package com.goodgame.profiling.drains;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import io.bifroest.commons.model.Metric;

public interface Drain extends Closeable {
    void flushRemainingBuffers() throws IOException;
    void output( List<Metric> metrics ) throws IOException;
}
