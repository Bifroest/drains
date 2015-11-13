package com.goodgame.profiling.drains.statistics;

import java.util.concurrent.atomic.LongAdder;

import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.commons.statistics.WriteToStorageEvent;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusManager;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusRegistrationPoint;
import com.goodgame.profiling.commons.statistics.gathering.StatisticGatherer;

@MetaInfServices
public class AsyncDrainQueueSizeReporter implements StatisticGatherer {
    LongAdder queueSize = new LongAdder();

    @Override
    public void init() {
        EventBusRegistrationPoint registrationPoint = EventBusManager.createRegistrationPoint();

        registrationPoint.subscribe( AsyncDrainQueueSizeChangedEvent.class, e -> queueSize.add( e.getDelta() ) );
        registrationPoint.subscribe( WriteToStorageEvent.class,
                e -> e.storageToWriteTo().getSubStorageCalled( "async-drain" ).store( "queue-size", queueSize.longValue() ) );
    }
}
