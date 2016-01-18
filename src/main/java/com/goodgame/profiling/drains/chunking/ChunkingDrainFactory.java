package com.goodgame.profiling.drains.chunking;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainWrapperFactory;

import io.bifroest.commons.boot.interfaces.Environment;

@MetaInfServices
public class ChunkingDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public String handledType() {
        return "chunked";
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.emptyList();
    }

    @Override
    public ChunkingDrain wrap( E environment, Drain inner, JSONObject subconfiguration, String name ) {
        return new ChunkingDrain( inner, subconfiguration.getInt( "chunksize" ) );
    }
}
