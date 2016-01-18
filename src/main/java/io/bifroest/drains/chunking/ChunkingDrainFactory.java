package io.bifroest.drains.chunking;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

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
