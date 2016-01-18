package io.bifroest.drains.serial.neverfail;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainMultipleWrapperFactory;

@MetaInfServices
public class NeverFailDrainFactory<E extends Environment> implements DrainMultipleWrapperFactory<E> {
    @Override
    public String handledType() {
        return "never-fail";
    }

    @Override
    public SerialNeverFailDrain wrap( E environment, List<Drain> inners, JSONObject config, String name ) {
        return new SerialNeverFailDrain( inners );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }
}
