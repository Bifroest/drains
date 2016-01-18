package io.bifroest.drains.repeated_flush;


import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;

import io.bifroest.commons.cron.TaskRunner;
import io.bifroest.commons.cron.TaskRunner.TaskID;
import io.bifroest.commons.logging.LogService;
import io.bifroest.commons.model.Metric;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;

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
