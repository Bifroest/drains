package com.goodgame.profiling.drains;

import com.goodgame.profiling.commons.boot.interfaces.Environment;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;

public interface DrainMultipleWrapperFactory<E extends Environment> {
    List<Class<? super E>> getRequiredEnvironments();
    default void addRequiredSystems( Collection<String> requiredSystems, JSONObject subconfiguration ) { }

    public String handledType();
    public Drain wrap ( E environment, List<Drain> inners, JSONObject subconfiguration, String name );
}
