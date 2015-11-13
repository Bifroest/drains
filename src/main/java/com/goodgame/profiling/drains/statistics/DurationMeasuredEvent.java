/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodgame.profiling.drains.statistics;

import java.time.Instant;
import java.util.Objects;

import com.goodgame.profiling.commons.statistics.eventbus.EventBusManager;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class DurationMeasuredEvent {
    private final String drainName;
    private final Instant start;
    private final Instant end;

    private DurationMeasuredEvent( String drainName, Instant start, Instant end ) {
        this.drainName = drainName;
        this.start = start;
        this.end = end;
    }

    public static void fire( String name, Instant start, Instant end ) {
        EventBusManager.fire( new DurationMeasuredEvent( name, start, end ) );
    }

    public String getDrainName() {
        return drainName;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode( this.drainName );
        hash = 73 * hash + Objects.hashCode( this.start );
        hash = 73 * hash + Objects.hashCode( this.end );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final DurationMeasuredEvent other = (DurationMeasuredEvent) obj;
        if ( !Objects.equals( this.drainName, other.drainName ) ) {
            return false;
        }
        if ( !Objects.equals( this.start, other.start ) ) {
            return false;
        }
        if ( !Objects.equals( this.end, other.end ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DurationMeasuredEvent{" + "drainName=" + drainName + ", start=" + start + ", end=" + end + '}';
    }


}
