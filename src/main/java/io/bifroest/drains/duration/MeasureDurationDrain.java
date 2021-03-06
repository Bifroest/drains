package io.bifroest.drains.duration;


import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.commons.statistics.percentile.PercentileEvent;
import io.bifroest.commons.statistics.percentile.PercentileTracker;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;
import io.bifroest.drains.statistics.DurationMeasuredEvent;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class MeasureDurationDrain extends AbstractWrappingDrain {
    private static final Clock clock = Clock.systemUTC();

    private final String name;
    private final String trackerIdentifier;
    public MeasureDurationDrain( Drain inner, String name ) {
        super( inner );
        this.name = name;

        trackerIdentifier = "measure-duration-" + name;
        PercentileTracker.make( trackerIdentifier, 1000, "drains.durations.percentiles." + name, .5, .9 );

    }

    @Override
    public void output(List<Metric> metrics) throws IOException {
        Instant start = clock.instant();
        try {
            inner.output( metrics );
        } finally {
            Instant end = clock.instant();
            DurationMeasuredEvent.fire( name, start, end );
            PercentileEvent.fire( trackerIdentifier, Duration.between( start, end ).toNanos() );
        }
    }
}
