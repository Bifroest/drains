package io.bifroest.drains.throttling;

import java.io.IOException;
import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;

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
