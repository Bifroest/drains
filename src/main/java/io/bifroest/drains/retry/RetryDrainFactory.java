package io.bifroest.drains.retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.commons.statistics.units.parse.DurationParser;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

@MetaInfServices
public class RetryDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }

    @Override
    public String handledType() {
        return "retry";
    }

    @Override
    public RetryDrain wrap( E environment, Drain inner, JSONObject subconfiguration, String name ) {
        return new RetryDrain( inner,
                               subconfiguration.getInt( "retry-count" ),
                               subconfiguration.has( "wait-time" )
                                       ? new DurationParser().parse( subconfiguration.getString( "wait-time" ) )
                                       : Duration.ZERO );
    }
}
