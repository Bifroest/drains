package com.goodgame.profiling.drains.repeated_flush;

import com.goodgame.profiling.commons.logging.LogService;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.commons.systems.cron.TaskRunner;
import com.goodgame.profiling.commons.systems.cron.TaskRunner.TaskID;
import com.goodgame.profiling.drains.AbstractWrappingDrain;
import com.goodgame.profiling.drains.Drain;

import org.slf4j.Logger;

public class RepeatedFlushDrain extends AbstractWrappingDrain {
    private static final Logger logger = LogService.getLogger(RepeatedFlushDrain.class);

    private final TaskID task;

    private final Object lock = new Object();

    public RepeatedFlushDrain( Drain inner, Duration frequency ) {
        super( inner );

        this.task = TaskRunner.runRepeated( new Flusher(), "repeating flush", Duration.ZERO, frequency, false );
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        synchronized( lock ) {
            super.output( metrics );
        }
    }

    @Override
    public void flushRemainingBuffers() throws IOException {
        synchronized( lock ) {
            super.flushRemainingBuffers();
        }
    }

    @Override
    public void close() throws IOException {
        synchronized( lock ) {
            TaskRunner.stopTask( task );
        }
        super.close();
    }

    private class Flusher implements Runnable {
        @Override
        public void run() {
            try {
                synchronized ( lock ) {
                    RepeatedFlushDrain.this.inner.flushRemainingBuffers();
                }
            } catch( IOException e ) {
                logger.warn( "Exception while flushing Buffers", e );
            }
        }
    }
}
