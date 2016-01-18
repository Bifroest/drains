package com.goodgame.profiling.drains.serial.neverfail;

import java.util.Collection;
import java.util.List;

import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.serial.AbstractSerialDrain;

import io.bifroest.commons.model.Metric;

public final class SerialNeverFailDrain extends AbstractSerialDrain {
    public SerialNeverFailDrain( Collection<Drain> subs ) {
        super( subs );
    }

    @Override
    public void output( List<Metric> metrics ) {
        forEachDrainLoggingExceptions( s -> s.output( metrics ) );
    }
}
