package io.bifroest.drains.statistics;

import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.configuration.EnvironmentWithJSONConfiguration;
import io.bifroest.commons.statistics.EnvironmentWithStatisticsGatherer;
import io.bifroest.commons.statistics.push_strategy.StatisticsPushStrategy;
import io.bifroest.commons.statistics.push_strategy.StatisticsPushStrategyFactory;
import io.bifroest.commons.statistics.push_strategy.with_task.StatisticsPushStrategyWithTask;
import io.bifroest.commons.statistics.units.parse.DurationParser;

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
