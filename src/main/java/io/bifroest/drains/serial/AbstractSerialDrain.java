package io.bifroest.drains.serial;


import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;

import io.bifroest.commons.logging.LogService;
import io.bifroest.drains.Drain;

public abstract class AbstractSerialDrain implements Drain {
    private final Logger logger = LogService.getLogger(AbstractSerialDrain.class);

    private final Collection<Drain> subs;

    protected AbstractSerialDrain( Collection<Drain> subs ) {
        this.subs = subs;
    }

    protected interface ConsumerWithIOException<T> {
        void accept( T t ) throws IOException;
    }

    protected Collection<Drain> drains() {
        return Collections.unmodifiableCollection( this.subs );
    }

    protected void forEachDrainLoggingExceptions( ConsumerWithIOException<Drain> c ) {
        subs.forEach( s -> {
            try {
                c.accept( s );
            } catch ( Exception e ) {
                logger.warn( "Drain " + s + " failed to do ", e );
            }
        });
    }

    @Override
    public void flushRemainingBuffers() {
        forEachDrainLoggingExceptions( Drain::flushRemainingBuffers );
    }

    @Override
    public void close() {
        forEachDrainLoggingExceptions( Drain::close );
    }
}
