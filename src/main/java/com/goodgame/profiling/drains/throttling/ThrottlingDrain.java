package com.goodgame.profiling.drains.throttling;

import java.io.IOException;
import java.util.List;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.drains.AbstractWrappingDrain;
import com.goodgame.profiling.drains.Drain;

public final class ThrottlingDrain extends AbstractWrappingDrain {
    private final Bucket bucket;

    public ThrottlingDrain( Drain inner, int burstSize, int metricsPerSecond ) {
        super( inner );
        this.bucket = new Bucket( burstSize, metricsPerSecond );
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        super.output( metrics );
        bucket.putIntoBucket( metrics.size() );
    }
}
