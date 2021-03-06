package io.bifroest.drains.async;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.SystemIdentifiers;
import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

@MetaInfServices
public class AsyncDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public String handledType() {
        return "queued";
    }

    @Override
    public Drain wrap( E environment, Drain inner, JSONObject config, String name ) {
        // In case this ever get's a config:
        // Make sure to FIRST load the config in a try-catch-block
        // and only if this succeeds, call the constructor
        return new AsyncDrain( inner, config.getInt( "max-queue-size" ), config.optBoolean( "use-percentile-tracker" ), name );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }

    @Override
    public void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) {
        requiredSystems.add( SystemIdentifiers.STATISTICS );
    }
}
