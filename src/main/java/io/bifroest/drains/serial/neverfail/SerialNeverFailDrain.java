package io.bifroest.drains.serial.neverfail;

import java.util.Collection;
import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.Drain;
import io.bifroest.drains.serial.AbstractSerialDrain;

public final class SerialNeverFailDrain extends AbstractSerialDrain {
    public SerialNeverFailDrain( Collection<Drain> subs ) {
        super( subs );
    }

    @Override
    public void output( List<Metric> metrics ) {
        forEachDrainLoggingExceptions( s -> s.output( metrics ) );
    }
}
