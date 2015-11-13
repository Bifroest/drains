package com.goodgame.profiling.drains.counting;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.commons.boot.interfaces.Environment;
import com.goodgame.profiling.commons.systems.SystemIdentifiers;
import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainWrapperFactory;

@MetaInfServices
public class CountingDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    public String handledType() {
        return "count-metrics";
    }

    @Override
    public void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) {
        requiredSystems.add( SystemIdentifiers.STATISTICS );
    }

    @Override
    public CountingDrain wrap( E environment, Drain inner, JSONObject config, String name ) {
        return new CountingDrain( inner, name );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }
}
