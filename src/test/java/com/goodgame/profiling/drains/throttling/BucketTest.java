package com.goodgame.profiling.drains.throttling;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.Instant;

import org.junit.Test;
import org.mockito.InOrder;

import io.bifroest.commons.util.stopwatch.AsyncClock;

public final class BucketTest {
    private Bucket subject;
    private StubSleepProvider sleepProvider;
    private Bucket.SleepProvider sleepMockForVerify;

    public void createSubject( int capacity, int drainPerSecond ) {
        sleepProvider = new StubSleepProvider();
        subject = new Bucket( capacity, drainPerSecond, sleepProvider.getClock(), sleepProvider );
        sleepMockForVerify = sleepProvider.getSleepMock();
    }

    @Test
    public void testInitialBucketWontSleepIfIDontPutInEnough() {
        createSubject( 10, 2 );
        subject.putIntoBucket( 10 );
        verify( sleepMockForVerify, never() ).sleep( any( Duration.class ) );
    }

    @Test
    public void testInitialBucketWillSleepIfIPutInEnough() {
        createSubject( 10, 2 );
        subject.putIntoBucket( 14 );
        verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 2 ) );
    }

    @Test
    public void testBucketIsDrainedCausingNoSleep() {
        createSubject( 10, 2 );
        subject.putIntoBucket( 6 );
        sleepProvider.getClock().setInstant( sleepProvider.getClock().instant().plus( Duration.ofSeconds( 1 ) ) );
        subject.putIntoBucket( 6 );
        verify( sleepMockForVerify, never() ).sleep( any( Duration.class ) );
    }

    @Test
    public void testBucketIsDrainedCausingReduced() {
        createSubject( 10, 2 );
        subject.putIntoBucket( 6 );
        sleepProvider.getClock().setInstant( sleepProvider.getClock().instant().plus( Duration.ofSeconds( 1 ) ) );
        subject.putIntoBucket( 8 );
        verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 1 ) );
    }

    @Test
    public void testMultipleInvocations() {
        createSubject( 10, 1 );
        subject.putIntoBucket( 7 );
        subject.putIntoBucket( 6 );
        subject.putIntoBucket( 5 );

        InOrder inOrder = inOrder( sleepMockForVerify );

        inOrder.verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 3 ) );
        inOrder.verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 5 ) );
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testMultipleInvocationsWithSleeps() {
        createSubject( 10, 1 );
        subject.putIntoBucket( 7 );
        sleepProvider.getClock().setInstant( sleepProvider.getClock().instant().plus( Duration.ofSeconds( 1 ) ) );
        subject.putIntoBucket( 6 );
        sleepProvider.getClock().setInstant( sleepProvider.getClock().instant().plus( Duration.ofSeconds( 1 ) ) );
        subject.putIntoBucket( 5 );
        sleepProvider.getClock().setInstant( sleepProvider.getClock().instant().plus( Duration.ofSeconds( 8 ) ) );
        subject.putIntoBucket( 5 );
        subject.putIntoBucket( 5 );

        InOrder inOrder = inOrder( sleepMockForVerify );

        inOrder.verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 2 ) );
        inOrder.verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 4 ) );
        inOrder.verify( sleepMockForVerify ).sleep( Duration.ofSeconds( 2 ) );
        inOrder.verifyNoMoreInteractions();
    }

    private static final class StubSleepProvider implements Bucket.SleepProvider {
        private final AsyncClock clock;
        private final Bucket.SleepProvider sleepMock;

        public StubSleepProvider() {
            this.clock = new AsyncClock();
            clock.setInstant( Instant.ofEpochSecond( 0 ) );

            this.sleepMock = mock( Bucket.SleepProvider.class );
        }

        @Override
        public void sleep( Duration d ) {
            clock.setInstant( clock.instant().plus( d ) );
            sleepMock.sleep( d );
        }

        public AsyncClock getClock() {
            return clock;
        }

        public Bucket.SleepProvider getSleepMock() {
            return sleepMock;
        }

        @Override
        public String toString() {
            return "StubSleepProvider [clock=" + clock + "]";
        }
    }
}
