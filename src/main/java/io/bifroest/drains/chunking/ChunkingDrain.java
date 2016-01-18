package io.bifroest.drains.chunking;

import java.io.IOException;
import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;

public final class ChunkingDrain extends AbstractWrappingDrain {
    private final int chunkSize;

    public ChunkingDrain( Drain inner, int chunkSize ) {
        super( inner );
        this.chunkSize = chunkSize;
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        for ( int fromIndex = 0; fromIndex < metrics.size(); fromIndex += chunkSize ) {
            super.output( metrics.subList( fromIndex, Math.min( fromIndex + chunkSize, metrics.size() ) ) );
        }
    }
}
