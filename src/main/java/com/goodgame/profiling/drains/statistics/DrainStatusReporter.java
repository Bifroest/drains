package com.goodgame.profiling.drains.statistics;

import java.util.HashMap;
import java.util.Map;

import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.commons.statistics.WriteToStorageEvent;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusManager;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusRegistrationPoint;
import com.goodgame.profiling.commons.statistics.gathering.StatisticGatherer;
import com.goodgame.profiling.commons.statistics.storage.MetricStorage;

@MetaInfServices
public class DrainStatusReporter implements StatisticGatherer {
    private Map<String, Integer> numMetrics = new HashMap<String, Integer>();

    @Override
    public void init() {
        EventBusRegistrationPoint registrationPoint = EventBusManager.createRegistrationPoint();

        registrationPoint.subscribe( DrainMetricOutputEvent.class, e -> {
            if ( !numMetrics.containsKey( e.getDrainID() ) ) {
                numMetrics.put( e.getDrainID(), e.getNumMetrics() );
            } else {
                numMetrics.put( e.getDrainID(), numMetrics.get( e.getDrainID() ) + e.getNumMetrics() );
            }
        } );

        registrationPoint.subscribe( WriteToStorageEvent.class, e -> {
            MetricStorage subStorage = e.storageToWriteTo().getSubStorageCalled( "metrics output" );

            for( String drainID : numMetrics.keySet() ) {
                subStorage.store( drainID, numMetrics.get( drainID ) );
            }
        });
    }
}
