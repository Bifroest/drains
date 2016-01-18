package io.bifroest.drains.bifroest;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.BasicDrainFactory;

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
