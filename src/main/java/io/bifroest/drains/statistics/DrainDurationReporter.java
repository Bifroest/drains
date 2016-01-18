/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bifroest.drains.statistics;


import org.kohsuke.MetaInfServices;

import io.bifroest.commons.statistics.WriteToStorageEvent;
import io.bifroest.commons.statistics.duration.PartitionedDurationStatistics;
import io.bifroest.commons.statistics.eventbus.EventBusManager;
import io.bifroest.commons.statistics.eventbus.EventBusRegistrationPoint;
import io.bifroest.commons.statistics.gathering.StatisticGatherer;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
@MetaInfServices
public class DrainDurationReporter implements StatisticGatherer {
    PartitionedDurationStatistics runtimes = new PartitionedDurationStatistics( 1000, 5, 10 );
    @Override
    public void init() {
        EventBusRegistrationPoint registrationPoint = EventBusManager.createRegistrationPoint();
        
        registrationPoint.subscribe( DurationMeasuredEvent.class, e -> runtimes.handleCall( e.getStart(), e.getDrainName(), e.getEnd()));
        
        registrationPoint.subscribe( WriteToStorageEvent.class, e -> runtimes.writeInto( e.storageToWriteTo().getSubStorageCalled( "drains.durations" ) ));
    }
}
