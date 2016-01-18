package io.bifroest.drains.duration;


import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
@MetaInfServices
public class MeasureDurationFactory<E extends Environment> implements DrainWrapperFactory<E> {

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }

    @Override
    public String handledType() {
        return "measure-duration";
    }

    @Override
    public Drain wrap( Environment environment, Drain inner, JSONObject subconfiguration, String name ) {
        return new MeasureDurationDrain( inner, name ); 
    }
}
