package com.goodgame.profiling.drains.buffering;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.goodgame.profiling.drains.AbstractWrappingDrain;
import com.goodgame.profiling.drains.Drain;

import org.slf4j.Logger;

import io.bifroest.commons.logging.LogService;
import io.bifroest.commons.model.Metric;
import io.bifroest.commons.statistics.units.SI_PREFIX;
import io.bifroest.commons.statistics.units.TIME_UNIT;
import io.bifroest.commons.statistics.units.format.TimeFormatter;

public class BufferingDrain extends AbstractWrappingDrain {
    private static final Logger log = LogService.getLogger(BufferingDrain.class);

    private final List<Metric> buffer = new LinkedList<>();
    private final int capacity;
    private final long warnLimitInMillis;
    private final String name;
    
    private long firstMetricReceivedTimestampMillis = Integer.MIN_VALUE;

    private static final TimeFormatter timeFormatter = new TimeFormatter(SI_PREFIX.MILLI, TIME_UNIT.SECOND);

    public BufferingDrain(int capacity, long warnLimitInMillis, Drain inner, String name) {
        super(inner);
        this.capacity = capacity;
        this.warnLimitInMillis = warnLimitInMillis;
        this.name = name;
    }

    @Override
    public synchronized void flushRemainingBuffers() throws IOException {
        try {
            sendAll();
        } finally {
            inner.flushRemainingBuffers();
        }
    }

    @Override
    public synchronized void close() throws IOException {
        if ( !buffer.isEmpty() ) {
            sendAll();
        }

        inner.close();
    }

    @Override
    public synchronized void output(List<Metric> metrics) throws IOException {
        for (Metric metric : metrics) {
            if ( buffer.isEmpty() ) {
                assert( firstMetricReceivedTimestampMillis == Integer.MIN_VALUE );
                firstMetricReceivedTimestampMillis = System.currentTimeMillis();
            }
            
            buffer.add(metric);
            optionallyEmptyBuffer();
        }
    }

    private void optionallyEmptyBuffer() throws IOException {
        if (buffer.size() >= capacity) {
            sendAll();
        }
    }

    private void sendAll() throws IOException {
        if (buffer.size() > 0) {
            long nowMillis = System.currentTimeMillis();
            if (nowMillis - firstMetricReceivedTimestampMillis > warnLimitInMillis) {
                log.warn("Metrics have been in buffer " + name + " for " + timeFormatter.format(nowMillis - firstMetricReceivedTimestampMillis));
            } else {
                log.debug("Metrics have been in buffer " + name + "for " + timeFormatter.format(nowMillis - firstMetricReceivedTimestampMillis));
            }
        }

        inner.output(buffer);
        buffer.clear();
        firstMetricReceivedTimestampMillis = Integer.MIN_VALUE;
    }
}
