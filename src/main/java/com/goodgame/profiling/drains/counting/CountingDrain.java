package com.goodgame.profiling.drains.counting;

import java.io.IOException;
import java.util.List;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusManager;
import com.goodgame.profiling.drains.AbstractWrappingDrain;
import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.statistics.DrainMetricOutputEvent;

public final class CountingDrain extends AbstractWrappingDrain {
    private final String drainId;

    public CountingDrain( Drain inner, String drainId ) {
        super( inner );
        this.drainId = drainId;
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        EventBusManager.fire( new DrainMetricOutputEvent( drainId, metrics.size() ) );

        super.output( metrics );
    }
}
