package com.goodgame.profiling.drains.statistics;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.commons.statistics.units.parse.DurationParser;
import com.goodgame.profiling.commons.systems.configuration.EnvironmentWithJSONConfiguration;
import com.goodgame.profiling.commons.systems.statistics.EnvironmentWithStatisticsGatherer;
import com.goodgame.profiling.commons.systems.statistics.push_strategy.StatisticsPushStrategy;
import com.goodgame.profiling.commons.systems.statistics.push_strategy.StatisticsPushStrategyFactory;
import com.goodgame.profiling.commons.systems.statistics.push_strategy.with_task.StatisticsPushStrategyWithTask;
import com.goodgame.profiling.drains.statistics.DirectlyToDrainPushStrategy;

@MetaInfServices
public class DirectlyToDrainPushStrategyFactory<E extends EnvironmentWithJSONConfiguration & EnvironmentWithStatisticsGatherer> implements StatisticsPushStrategyFactory<E> {
    @Override
    public StatisticsPushStrategy<E> create( JSONObject config ) {
        StatisticsPushStrategyWithTask<E> strategy = new DirectlyToDrainPushStrategy<E>(
                config.getString( "base" ),
                ( new DurationParser() ).parse( config.getString( "each" ) ),
                config.getString( "type" ),
                config.getJSONObject( "drain" ) );
        return strategy;
    }

    @Override
    public String handledType() {
        return "internal";
    }
}
