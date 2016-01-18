package io.bifroest.drains.throttling;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

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
