package io.bifroest.drains;

import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import io.bifroest.commons.boot.interfaces.Environment;

public interface BasicDrainFactory<E extends Environment>  {
    List<Class<? super E>> getRequiredEnvironments();
    default void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) { }

    String handledType();
    Drain create( E environment, JSONObject subconfiguration, String name );
}
