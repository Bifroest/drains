package com.goodgame.profiling.drains;

import java.io.IOException;

public abstract class AbstractBasicDrain implements Drain {
    @Override
    public void close() throws IOException {
        // Do nothing
    }

    @Override
    public void flushRemainingBuffers() throws IOException {
        // Do nothing

    }
}
