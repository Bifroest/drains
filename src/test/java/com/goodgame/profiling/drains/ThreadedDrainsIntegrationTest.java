package com.goodgame.profiling.drains;

import static org.subquark.mtdt.generators.PrimitiveGenerators.DOUBLES;
import static org.subquark.mtdt.generators.PrimitiveGenerators.LONGS;
import static org.subquark.mtdt.generators.ObjectGenerator.randomInstances;
import static org.subquark.mtdt.generators.StringGenerator.mostlyReadableStrings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Test;
import org.subquark.mtdt.generators.InputGenerator;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.async.AsyncDrain;
import com.goodgame.profiling.drains.buffering.BufferingDrain;
import com.goodgame.profiling.drains.filterU.FilterUDrain;
import com.goodgame.profiling.rewrite_framework_test.integration.ExpectationBasedDrain;

public class ThreadedDrainsIntegrationTest {
    @Test
    public void testFoo() throws IOException {
        InputGenerator<Metric> metricGenerator = randomInstances(
                Metric::new, mostlyReadableStrings(8), LONGS.butOnly( t -> t > 0 ), DOUBLES);
        InputGenerator<List<Metric>> threadInput = metricGenerator.repeat(200, 500);

        Random r = new Random();

        List<List<Metric>> inputs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            inputs.add(threadInput.generateRandomInput(r));
        }
        List<Metric> expectation = new ArrayList<>();
        inputs.stream().forEach(expectation::addAll);

        ExpectationBasedDrain expectationDrain = new ExpectationBasedDrain( expectation.toArray( new Metric[0] ) );
        final Drain subject = new FilterUDrain( new AsyncDrain( new BufferingDrain ( 20, 5000, expectationDrain, "buffer" ), 1000 ) );

        List<Thread> threads = inputs.stream().map( l -> new Thread( () -> { 
            try {
                subject.output(l);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } ) ).collect( Collectors.toList() );
        threads.forEach( t -> t.start() );
        threads.forEach( t -> {
            try {
                System.out.println("Joining " + t);
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } );

        subject.flushRemainingBuffers();
        subject.close();
    }
}
