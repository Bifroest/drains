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
public class PersistentDrainNames implements NamingScheme {
    private final NamingScheme inner;
 
    public PersistentDrainNames( NamingScheme inner ) {
        this.inner = inner;
    }

    @Override
    public String nameDrain( JSONObject config ) {
        if ( config.getString( "type" ).equals( "persistent" ) ) {
            return "persistent-" + config.getString( "id" );
        } else {
            return inner.nameDrain( config );
        }
    }    
}
