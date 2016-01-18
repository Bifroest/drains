package io.bifroest.drains.statistics;

import java.util.concurrent.atomic.LongAdder;

import org.kohsuke.MetaInfServices;

import io.bifroest.commons.statistics.WriteToStorageEvent;
import io.bifroest.commons.statistics.eventbus.EventBusManager;
import io.bifroest.commons.statistics.eventbus.EventBusRegistrationPoint;
import io.bifroest.commons.statistics.gathering.StatisticGatherer;


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
