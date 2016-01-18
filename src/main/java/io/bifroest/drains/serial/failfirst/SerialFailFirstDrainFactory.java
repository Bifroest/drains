package io.bifroest.drains.serial.failfirst;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainMultipleWrapperFactory;

@MetaInfServices
public class SerialFailFirstDrainFactory<E extends Environment> implements DrainMultipleWrapperFactory<E> {
    @Override
    public String handledType() {
        return "fail-first";
    }

    @Override
    public SerialFailFirstDrain wrap( E environment, List<Drain> inners, JSONObject config, String name ) {
        return new SerialFailFirstDrain( inners );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }
}
