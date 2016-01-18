package io.bifroest.drains.debug;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;

public final class DebugLoggingDrain extends AbstractWrappingDrain {
    private final String grep;

    private final Logger metricLogger;
    
    public DebugLoggingDrain( Drain inner ) {
        this( inner, null );
    }

    public DebugLoggingDrain( Drain inner, String grep ) {
        this( inner, grep, "unnamed" );
    }
    
    public DebugLoggingDrain( Drain inner, String grep, String name ) {
        super( inner );
        this.grep = grep;
        metricLogger = LogManager.getLogger( DebugLoggingDrain.class.getName() + "." + name );
        metricLogger.debug( "Creating DebugLoggingDrain" );
    }

    @Override
    public void flushRemainingBuffers() throws IOException {
        metricLogger.debug( "flushing DebugLoggingDrain" );
        inner.flushRemainingBuffers();
    }

    @Override
    public void close() throws IOException {
        metricLogger.debug( "flushing+closing DebugLoggingDrain" );
        inner.close();
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        if ( metricLogger.isDebugEnabled() ) {
            if ( grep == null ) {
                metricLogger.debug( StringUtils.join( metrics, "|" ) );
            } else {
                List<Metric> filteredMetrics = metrics.stream()
                                                      .filter( metric -> metric.name().contains( grep ) )
                                                      .collect( Collectors.toList() );
                if ( !filteredMetrics.isEmpty() ) {
                    metricLogger.debug( StringUtils.join( filteredMetrics, "|" ) );
                }
            }
        }
        inner.output( metrics );
    }
}
