package com.goodgame.profiling.drains.bifroest;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.drains.BasicDrainFactory;

import io.bifroest.commons.boot.interfaces.Environment;

@MetaInfServices
public class BifroestDrainFactory<E extends Environment> implements BasicDrainFactory<E> {
    @Override
    public String handledType() {
        return "bifroest";
    }

    @Override
    public BifroestDrain create( E environment, JSONObject config, String name ) {
        return new BifroestDrain( config.getString( "host" ), config.getInt( "port" ) );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.<Class<? super E>> emptyList();
    }
}
