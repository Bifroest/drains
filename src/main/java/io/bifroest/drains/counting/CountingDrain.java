package io.bifroest.drains.counting;

import java.io.IOException;
import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.commons.statistics.eventbus.EventBusManager;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;
import io.bifroest.drains.statistics.DrainMetricOutputEvent;

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
