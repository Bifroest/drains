package com.goodgame.profiling.drains.async;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.statistics.AsyncDrainQueueSizeChangedEvent;

import io.bifroest.commons.model.Metric;
import io.bifroest.commons.statistics.eventbus.EventBusManager;
import io.bifroest.commons.statistics.percentile.PercentileEvent;
import io.bifroest.commons.statistics.percentile.PercentileTracker;

public class AsyncDrain implements Drain {
    private static final Logger log = LogManager.getLogger();

    private static final Clock clock = Clock.systemUTC();

    private final Drain innerDrain;

    private final BlockingQueue<Metric> outputQueue;
    private final int maxQueueSize;

    // Use a executor service, so dying threads are restarted. This needs to be single-threaded.
    private final ExecutorService queueConsumerExecutor;
    private final Map<String, String> threadContextMap;

    private volatile boolean stopped;

    private final Object lock = new Object();

    private final String trackerIdentifier;
    
    public AsyncDrain( Drain innerDrain, int maxQueueSize ) {
        this( innerDrain, maxQueueSize, false, "unnamed" );
    }

    public AsyncDrain( Drain innerDrain, int maxQueueSize, boolean usePercentileTracker, String name ) {
        this.innerDrain = innerDrain;

        this.maxQueueSize = maxQueueSize;
        this.outputQueue = new LinkedBlockingQueue<>( maxQueueSize );

        threadContextMap = new HashMap<>( ThreadContext.getContext() );

        this.queueConsumerExecutor = Executors.newSingleThreadExecutor( r -> {
            Thread t = new Thread( r );
            t.setName( "Queue Consumer Thread" );
            return t;
        });
        this.queueConsumerExecutor.submit( new QueueConsumer( 0 ) );

        trackerIdentifier = "async-drain-" + name;
        if ( usePercentileTracker ) {
            String metricName = "drains." + name + ".take-sizes.percentiles";
            PercentileTracker.make( trackerIdentifier, 1000, metricName, 0, .1, .2, .5, .8, .9, 1 );
        }
    }

    @Override
    public void flushRemainingBuffers() throws IOException {
        try {
            log.debug( "Sending remaining buffers" );
            sendRemainingBuffer();
        } finally {
            log.debug( "Flushing inner drain" );
            innerDrain.flushRemainingBuffers();
            log.debug( "All metrics flushed" );
        }
    }

    @Override
    public void close() throws IOException {
        log.debug( "Stopping queue consumer" );
        stopped = true;
        log.debug( "Shutting down executor service" );
        queueConsumerExecutor.shutdownNow();
        log.debug( "Awaiting termination of executor service" );
        try {
            queueConsumerExecutor.awaitTermination( Long.MAX_VALUE, TimeUnit.DAYS );
        } catch( InterruptedException e ) {
            log.warn( "Interrupted while awaiting termination of queue consumer", e );
        }
        log.debug( "Async executor thread shut down." );
        sendRemainingBuffer();
        log.debug( "Remaining buffers sent" );
        innerDrain.close();
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        for( Metric metric : metrics ) {
            try {
                outputQueue.put( metric );
                fireAsyncDrainQueueSizeEvent( 1 );
            } catch( InterruptedException e ) {
                log.warn( "Interrupted while trying to add metrics to queue", e );
            }
        }
    }

    private void fireAsyncDrainQueueSizeEvent( int delta ) {
        EventBusManager.fire( new AsyncDrainQueueSizeChangedEvent( clock.instant(), delta ) );
        if ( delta < 0 ) {
            PercentileEvent.fire( trackerIdentifier, -delta );
        }
    }

    private void sendRemainingBuffer() throws IOException {
        log.debug( "sending " + outputQueue.size() + " elements " );
        List<Metric> metrics = new ArrayList<>( maxQueueSize );
        do {
            metrics.clear();
            synchronized( this.lock ) {
                outputQueue.drainTo( metrics, maxQueueSize );
                fireAsyncDrainQueueSizeEvent( -metrics.size() );
                innerDrain.output( metrics );
            }
        } while( metrics.size() != 0 );
    }

    private class QueueConsumer implements Runnable {
        private final int nr;

        public QueueConsumer( final int nr ) {
            this.nr = nr;
            log.trace( "Created queue consumer {}", nr );
        }

        @Override
        public void run() {
            threadContextMap.forEach( ( key, value ) -> ThreadContext.put( key, value ) );
            ThreadContext.put( "thread", "async-thread of " + threadContextMap.get( "thread" ) );
            log.trace( "QueueConsumer {} up and running", nr );

            try {
                if( !stopped ) {
                    try {
                        // Simulate a loop by rescheduling ourselves.
                        queueConsumerExecutor.submit( new QueueConsumer( nr + 1 ) );
                    } catch ( RejectedExecutionException e ) {
                        if ( stopped ) {
                            // ignore. This can happen, if we get shut down between the if and the submit above.
                        } else {
                            throw e;
                        }
                    }

                    List<Metric> metrics = new ArrayList<>( maxQueueSize );

                    // Take blocks, so we don't run in a hot-loop
                    Metric metric = outputQueue.take();
                    metrics.add( metric );

                    synchronized( AsyncDrain.this.lock ) {
                        // Be performant: drain as much as possible. Unfortunately,
                        // there is no blocking version of this, so we need the take() above
                        outputQueue.drainTo( metrics, maxQueueSize );

                        fireAsyncDrainQueueSizeEvent( -metrics.size() );

                        innerDrain.output( metrics );
                    }
                }
            } catch( IOException | RuntimeException e ) {
                log.warn( "Sending data failed.", e );
            } catch( InterruptedException e ) {
                log.debug( "QueueConsumer {} interrupted.", nr );
                return;
            }
            log.trace( "QueueConsumer {} exited - a new one should spawn right now.", nr );
        }
    }
}
