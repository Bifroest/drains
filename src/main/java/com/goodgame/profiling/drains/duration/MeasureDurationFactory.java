package com.goodgame.profiling.drains.duration;

import com.goodgame.profiling.commons.boot.interfaces.Environment;
import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainWrapperFactory;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

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
