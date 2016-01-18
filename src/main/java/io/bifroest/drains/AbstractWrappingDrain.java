package io.bifroest.drains;

import java.io.IOException;
import java.util.List;

import io.bifroest.commons.model.Metric;


public abstract class AbstractWrappingDrain implements Drain {
    protected final Drain inner;

    public AbstractWrappingDrain(Drain inner) {
        this.inner = inner;
    }

    @Override
    public void close() throws IOException {
        inner.close();
    }

    @Override
    public void flushRemainingBuffers() throws IOException {
        inner.flushRemainingBuffers();
    }

    @Override
    public void output(List<Metric> metrics) throws IOException {
        inner.output(metrics);
    }
}
