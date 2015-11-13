package com.goodgame.profiling.drains.throttling;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import org.junit.Test;

public final class BucketIntegrationTest {
    private static final int NR_OF_INVOCATIONS = 100;
    private static final int MAX_AMOUNT_PER_INVOCATION = 200;
    private static final int CAPACITY = 100;
    private static final int DRAIN_PER_SECOND = 10_000;

    @Test
    public void test() {
        Bucket subject = new Bucket( CAPACITY, DRAIN_PER_SECOND );
        Random r = new Random();

        int sum = 0;
        Instant start = Instant.now();
        for ( int i = 0; i < NR_OF_INVOCATIONS; i++ ) {
            int amount = r.nextInt( MAX_AMOUNT_PER_INVOCATION ) + 1;
            subject.putIntoBucket( amount );
            sum += amount;
        }

        Instant end = Instant.now();
        Duration timeSpent = Duration.between( start, end );

        Duration shouldNeedAtLeast = Duration.ofSeconds( sum - CAPACITY ).dividedBy( DRAIN_PER_SECOND );

        System.out.println( "Time spent: " + timeSpent );
        System.out.println( "Should need at least: " + shouldNeedAtLeast );
        System.out.println( "Average amount: " + ( sum / NR_OF_INVOCATIONS ) );
        assertTrue( timeSpent.compareTo( shouldNeedAtLeast ) > 0 );
    }
}
