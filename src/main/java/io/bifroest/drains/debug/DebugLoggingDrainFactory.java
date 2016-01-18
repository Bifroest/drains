package io.bifroest.drains.debug;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

@MetaInfServices
public class DebugLoggingDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public String handledType() {
        return "debug logging";
    }

    @Override
    public DebugLoggingDrain wrap( E environment, Drain inner, JSONObject config, String name ) {
        return new DebugLoggingDrain( inner,
                                      config.optString( "grep", null ),
                                      name );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }
}
