/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodgame.profiling.names;

import org.json.JSONObject;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class OverrideWithDrainId implements NamingScheme {
    private final NamingScheme inner;
    
    public OverrideWithDrainId( NamingScheme inner ) {
        this.inner = inner;
    }

    @Override
    public String nameDrain( JSONObject config ) {
        if ( config.has( "drain-id" ) ) {
            return config.getString( "drain-id" );
        }
        if ( config.has( "name" ) ) {
            return config.getString( "name" );
        }
        return inner.nameDrain( config );
    }
}
