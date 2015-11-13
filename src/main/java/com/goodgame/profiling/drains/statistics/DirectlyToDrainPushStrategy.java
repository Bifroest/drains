package com.goodgame.profiling.drains.statistics;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.commons.systems.configuration.EnvironmentWithJSONConfiguration;
import com.goodgame.profiling.commons.systems.statistics.EnvironmentWithStatisticsGatherer;
import com.goodgame.profiling.commons.systems.statistics.push_strategy.with_task.StatisticsPushStrategyWithTask;
import com.goodgame.profiling.drains.Drain;
import com.goodgame.profiling.drains.DrainCreator;
import com.goodgame.profiling.drains.DrainFactory;

public class DirectlyToDrainPushStrategy<E extends EnvironmentWithStatisticsGatherer & EnvironmentWithJSONConfiguration>
    extends StatisticsPushStrategyWithTask<E> {
    private static final Logger log = LogManager.getLogger();

    private final JSONObject drainConfig;

    public DirectlyToDrainPushStrategy( String metricPrefix, Duration each, String strategyName, JSONObject drainConfig ) {
        super( metricPrefix, each, strategyName );
        this.drainConfig = drainConfig;
    }

    @Override
    public void addMoreRequirements( List<String> destination ) {
        new DrainFactory<>().addRequirements( destination, drainConfig );
    }

    @Override
    public void pushAll( Collection<Metric> metrics ) {
        log.entry( metrics );

        Drain drain = new DrainCreator<E>().loadFromDrainConfiguration( environment, drainConfig );
        try {
            drain.output( new ArrayList<>( metrics ) );
        } catch ( IOException e ) {
            log.warn( "Exception while outputting metrics", e );
        } finally {
            try {
                drain.close();
            } catch( IOException e ) {
                log.warn( "Exception while closing drain", e );
            }
        }

        log.exit();
    }
}
