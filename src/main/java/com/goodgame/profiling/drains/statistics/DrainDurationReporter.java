/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodgame.profiling.drains.statistics;

import com.goodgame.profiling.commons.statistics.WriteToStorageEvent;
import com.goodgame.profiling.commons.statistics.duration.PartitionedDurationStatistics;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusManager;
import com.goodgame.profiling.commons.statistics.eventbus.EventBusRegistrationPoint;
import com.goodgame.profiling.commons.statistics.gathering.StatisticGatherer;
import org.kohsuke.MetaInfServices;

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
