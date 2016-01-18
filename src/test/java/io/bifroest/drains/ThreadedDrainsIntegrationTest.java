package io.bifroest.drains;


import java.io.IOException;

import org.junit.Test;

public class ThreadedDrainsIntegrationTest {
    @Test
    public void testFoo() throws IOException {
        // commented out, because the rewrite framework test stuff wasn't exported.
        /*
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
        */
    }
}
