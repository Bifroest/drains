package io.bifroest.drains;

import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import io.bifroest.commons.boot.interfaces.Environment;

public interface DrainWrapperFactory<E extends Environment> {
    List<Class<? super E>> getRequiredEnvironments();
    default void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) { }

    String handledType();
    Drain wrap( E environment, Drain inner, JSONObject subconfiguration, String name );
}
