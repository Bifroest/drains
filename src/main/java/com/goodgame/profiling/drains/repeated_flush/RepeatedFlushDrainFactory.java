package com.goodgame.profiling.drains.repeated_flush;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainWrapperFactory;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.commons.statistics.units.parse.DurationParser;

@MetaInfServices
public class RepeatedFlushDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }

    @Override
    public String handledType() {
        return "repeated-flush";
    }

    @Override
    public RepeatedFlushDrain wrap( E environment, Drain inner, JSONObject subconfiguration, String name ) {
        return new RepeatedFlushDrain( inner,
                                       new DurationParser().parse( subconfiguration.getString( "each" ) ) );
    }
}
