package com.goodgame.profiling.drains.bifroest;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.commons.util.json.JSONClient;
import com.goodgame.profiling.drains.AbstractBasicDrain;

public final class BifroestDrain extends AbstractBasicDrain {
    private final JSONClient bifroest;

    public BifroestDrain( String bifroestHost, int bifroestPort ) {
        this.bifroest = new JSONClient( bifroestHost, bifroestPort );
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        if( metrics.size() == 0 ) {
            return;
        }

        JSONArray metricsArray = new JSONArray();
        for ( Metric metric : metrics ) {
            JSONObject metricJson = new JSONObject();
            metricJson.put( "name", metric.name() );
            metricJson.put( "timestamp", metric.timestamp() );
            metricJson.put( "value", metric.value() );
            metricsArray.put( metricJson );
        }

        JSONObject command = new JSONObject();
        command.put( "command", "include-metrics" );
        command.put( "metrics", metricsArray );

        bifroest.request( command );
    }
}
