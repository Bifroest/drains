package io.bifroest.drains.empty;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.BasicDrainFactory;

@MetaInfServices
public class EmptyDrainFactory<E extends Environment> implements BasicDrainFactory<E> {
    @Override
    public String handledType() {
        return "void";
    }

    @Override
    public EmptyDrain create( E environment, JSONObject config, String name ) {
        return new EmptyDrain();
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.<Class<? super E>> emptyList();
    }
}
