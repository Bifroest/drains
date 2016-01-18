package com.goodgame.profiling.drains;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import io.bifroest.commons.model.Metric;

public class StubDrain implements Drain {
    private boolean flushRemainingBuffersCalled = false;
    private boolean closeCalled = false;
    private List<Metric> metricsOutputted = new LinkedList<>();

    @Override
    public synchronized void flushRemainingBuffers() throws IOException {
        flushRemainingBuffersCalled = true;
    }
    
    @Override
    public synchronized void close() throws IOException {
        closeCalled = true;
    }

    @Override
    public synchronized void output(List<Metric> metrics) throws IOException {
        metricsOutputted.addAll(metrics);
    }

    public synchronized boolean isFlushRemainingBuffersCalled() {
        return flushRemainingBuffersCalled;
    }
    
    public synchronized boolean isCloseCalled() {
        return closeCalled;
    }

    public synchronized List<Metric> getMetricsOutputted() {
        return metricsOutputted;
    }
}
