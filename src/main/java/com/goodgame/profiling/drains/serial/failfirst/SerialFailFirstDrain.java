package com.goodgame.profiling.drains.serial.failfirst;

import java.io.IOException;
import java.util.List;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.serial.AbstractSerialDrain;

public class SerialFailFirstDrain extends AbstractSerialDrain {
    public SerialFailFirstDrain( List<Drain> inners ) {
        super( inners );
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        for( Drain drain : drains() ) {
            // Do NOT catch exceptions in here
            // If one Drain throws an exception, we do NOT output to the other
            // inner Drains!
            drain.output( metrics );
        }
    }
}
