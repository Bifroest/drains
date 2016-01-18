package io.bifroest.drains.statistics;

import java.time.Instant;

public final class AsyncDrainQueueSizeChangedEvent {
    private final Instant when;
    private final int delta;

    public AsyncDrainQueueSizeChangedEvent( Instant when, int delta ) {
        this.when = when;
        this.delta = delta;
    }

    public Instant getWhen() {
        return when;
    }

    public int getDelta() {
        return delta;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + delta;
        result = prime * result + ( ( when == null ) ? 0 : when.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        AsyncDrainQueueSizeChangedEvent other = (AsyncDrainQueueSizeChangedEvent)obj;
        if ( delta != other.delta )
            return false;
        if ( when == null ) {
            if ( other.when != null )
                return false;
        } else if ( !when.equals( other.when ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AsyncDrainQueueSizeChangedEvent [when=" + when + ", delta=" + delta + "]";
    }
}
