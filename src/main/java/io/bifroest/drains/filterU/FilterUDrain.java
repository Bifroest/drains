package io.bifroest.drains.filterU;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.AbstractWrappingDrain;
import io.bifroest.drains.Drain;

/**
 * A value of "U" means "undefined" in Munin. If a drain cannot handle those,
 * this wrapper can filter them out.
 * 
 * @author sglimm
 */
public class FilterUDrain extends AbstractWrappingDrain {
    public FilterUDrain(Drain inner) {
        super(inner);
    }

    @Override
    public void output(List<Metric> metrics) throws IOException {
        List<Metric> filteredMetrics = new ArrayList<>(metrics.size());

        for (Metric metric : metrics) {
            if (!Double.isNaN(metric.value())) {
                filteredMetrics.add(metric);
            }
        }

        inner.output(filteredMetrics);
    }

}
