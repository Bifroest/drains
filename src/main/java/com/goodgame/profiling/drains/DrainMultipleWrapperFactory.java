package com.goodgame.profiling.drains;

import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import io.bifroest.commons.boot.interfaces.Environment;

public interface DrainMultipleWrapperFactory<E extends Environment> {
    List<Class<? super E>> getRequiredEnvironments();
    default void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) { }

    public String handledType();
    public Drain wrap ( E environment, List<Drain> inners, JSONObject subconfiguration, String name );
}
