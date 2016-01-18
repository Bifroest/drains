package io.bifroest.drains.statistics;

public final class DrainMetricOutputEvent {
    private final String drainID;
    private final int numMetrics;

    public DrainMetricOutputEvent( String drainID, int numMetrics ) {
        this.drainID = drainID;
        this.numMetrics = numMetrics;
    }

    public String getDrainID() {
        return drainID;
    }

    public int getNumMetrics() {
        return numMetrics;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( drainID == null ) ? 0 : drainID.hashCode() );
        result = prime * result + numMetrics;
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
        DrainMetricOutputEvent other = (DrainMetricOutputEvent)obj;
        if ( drainID == null ) {
            if ( other.drainID != null )
                return false;
        } else if ( !drainID.equals( other.drainID ) )
            return false;
        if ( numMetrics != other.numMetrics )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DrainMetricOutputEvent [drainID=" + drainID + ", numMetrics=" + numMetrics + "]";
    }
}
