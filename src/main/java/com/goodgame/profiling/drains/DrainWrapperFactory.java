package com.goodgame.profiling.drains;

import com.goodgame.profiling.commons.boot.interfaces.Environment;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;

public interface DrainWrapperFactory<E extends Environment> {
    List<Class<? super E>> getRequiredEnvironments();
    default void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) { }

    String handledType();
    Drain wrap( E environment, Drain inner, JSONObject subconfiguration, String name );
}
