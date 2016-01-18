package com.goodgame.profiling.drains.filterU;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainWrapperFactory;

import io.bifroest.commons.boot.interfaces.Environment;

@MetaInfServices
public class FilterUDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public String handledType() {
        return "filter-u";
    }

    @Override
    public Drain wrap( E environment, Drain inner, JSONObject config, String name ) {
        return new FilterUDrain(inner);
    }

	@Override
	public List<Class<? super E>> getRequiredEnvironments() {
		return Collections.<Class<? super E>>emptyList();
	}
}
