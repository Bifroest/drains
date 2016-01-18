package io.bifroest.drains.statistics;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import io.bifroest.commons.configuration.EnvironmentWithJSONConfiguration;
import io.bifroest.commons.model.Metric;
import io.bifroest.commons.statistics.EnvironmentWithStatisticsGatherer;
import io.bifroest.commons.statistics.push_strategy.with_task.StatisticsPushStrategyWithTask;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainCreator;
import io.bifroest.drains.DrainFactory;

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
