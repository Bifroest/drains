package io.bifroest.drains.throttling;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * Rate limiting implementation with a leaky bucket.
 * @author sglimm
 */
public final class Bucket {
    private final Clock clock;
    private final int capacity;
    private final int drainPerSecond;
    private final SleepProvider s;

    private int fillLevel;
    private Instant lastUpdate;

    // for testing
    Bucket( int capacity, int drainPerSecond, Clock clock, SleepProvider s ) {
        this.clock = clock;
        this.capacity = capacity;
        this.drainPerSecond = drainPerSecond;
        this.s = s;

        fillLevel = 0;
        lastUpdate = clock.instant();
    }

    public Bucket( int capacity, int drainPerSecond ) {
        this.clock = Clock.systemUTC();
        this.capacity = capacity;
        this.drainPerSecond = drainPerSecond;
        this.s = new DefaultSleepProvider( this.clock );

        fillLevel = 0;
        lastUpdate = this.clock.instant();
    }

    private int potentiallyAmountDrainedIn( Duration d ) {
        long result = d.toNanos() * drainPerSecond / 1_000_000_000;
        // For very long durations, this might overflow. Returning the largest integer possible
        // is better than just truncating. That way, the Math.max() in elapseTime will always
        // result in a fillLevel of 0;
        return result > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)result;
    }

    private void elapseTime() {
        Instant now = clock.instant();
        Duration elapsed = Duration.between( lastUpdate, now );
        fillLevel = Math.max( fillLevel - potentiallyAmountDrainedIn( elapsed ), 0 );
        lastUpdate = now;
    }

    /**
     * @param amount how much to fill into the bucket
     * @return how much of the amount couldn't be filled into the bucket. If > 0, we have to sleep.
     */
    private int tryToFillBucketWithoutSleeping( int amount ) {
        int possibleAmount = Math.min( amount, capacity - fillLevel );
        fillLevel += possibleAmount;
        return amount - possibleAmount;
    }

    private void fillBucketWithSleeping( int amount ) {
        s.sleep( Duration.ofSeconds( amount ).dividedBy( drainPerSecond ) );
        lastUpdate = clock.instant();
    }

    public synchronized void putIntoBucket( int amount ) {
        elapseTime();
        int remainingAmount = tryToFillBucketWithoutSleeping( amount );
        if ( remainingAmount > 0 ) {
            fillBucketWithSleeping( remainingAmount );
        }
    }

    @Override
    public String toString() {
        return "Bucket [capacity=" + capacity + ", drainPerSecond="
                + drainPerSecond + ", fillLevel=" + fillLevel + ", lastUpdate="
                + lastUpdate + "]";
    }

    /*
     * This is an interface, so that we can mock Thread.sleep() away.
     */
    public interface SleepProvider {
        public void sleep( Duration d );
    }

    private static final class DefaultSleepProvider implements SleepProvider {
        private final Clock clock;

        public DefaultSleepProvider( Clock clock ) {
            this.clock = clock;
        }

        public void sleep( Duration d ) {
            Duration remaining = d;
            Instant target = clock.instant().plus( d );
            while ( remaining.compareTo( Duration.ZERO ) > 0 ) {
                // Make sure, we wait AT LEAST d. No Interruptions, no rounding errors allowed.
                try {
                    Thread.sleep( remaining.toMillis() );
                } catch ( InterruptedException e ) {
                    // ignored
                }
                remaining = Duration.between( clock.instant(), target );
            }
        }
    }
}
