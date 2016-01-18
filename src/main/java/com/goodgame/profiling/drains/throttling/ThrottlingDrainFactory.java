package com.goodgame.profiling.drains.throttling;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainWrapperFactory;

import io.bifroest.commons.boot.interfaces.Environment;

@MetaInfServices
public final class ThrottlingDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }

    @Override
    public String handledType() {
        return "throttling";
    }

    @Override
    public Drain wrap( E environment, Drain inner, JSONObject subconfiguration, String name ) {
        return new ThrottlingDrain( inner,
                                    subconfiguration.getInt( "burst-size" ),
                                    subconfiguration.getInt( "metrics-per-second") );
    }
}
