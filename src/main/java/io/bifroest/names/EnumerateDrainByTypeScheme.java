/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bifroest.names;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class EnumerateDrainByTypeScheme implements NamingScheme {
    private final HashMap<String, AtomicInteger> typeToDrainsNamedSoFar;
    
    public EnumerateDrainByTypeScheme() {
        typeToDrainsNamedSoFar = new HashMap<>();
    }
    
    private int getNextDrainIndex( String type ) {
        AtomicInteger currentCount;
        if ( typeToDrainsNamedSoFar.containsKey( type ) ) {
            currentCount = typeToDrainsNamedSoFar.get( type );
        } else {
            currentCount = new AtomicInteger( 0 );
            typeToDrainsNamedSoFar.put( type, currentCount );            
        }
        return currentCount.incrementAndGet();
    }
    
    @Override
    public String nameDrain( JSONObject config ) {
        String type = config.getString(  "type" );
        int nextIndex = getNextDrainIndex( type );
        return String.format( "%s%02d", type, nextIndex );
    }
    
}
