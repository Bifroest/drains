package com.goodgame.profiling.drains.retry;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.goodgame.profiling.drains.AbstractWrappingDrain;
import com.goodgame.profiling.drains.Drain;

import io.bifroest.commons.model.Metric;

public class RetryDrain extends AbstractWrappingDrain {
    private static final Logger log = LogManager.getLogger();

    private final int retryCount;
    private final Duration waitTime;

    public RetryDrain( Drain inner, int retryCount, Duration waitTime ) {
        super( inner );

        this.retryCount = retryCount;
        this.waitTime = waitTime;
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        List<Throwable> exceptions = new ArrayList<>();
        for ( int i = 0 ; i < retryCount ; i++ ) {
            try {
                super.output( metrics );
                return;
            } catch( Exception e ) {
                log.debug( "Caught exception", e );
                exceptions.add( e );
            }
            try {
                Thread.sleep( waitTime.toMillis() );
            } catch( InterruptedException e ) {
                // Ignored. If we get interrupted here, we don't care, we simply don't wait as long as configured.
            }
        }
        throw new RetryCountExceededException( exceptions );
    }
}
