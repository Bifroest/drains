package com.goodgame.profiling.names;

import org.json.JSONObject;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class SpecialBifroestDrainNames implements NamingScheme {
    private final NamingScheme inner;

    public SpecialBifroestDrainNames( NamingScheme inner ) {
        this.inner = inner;
    }

    @Override
    public String nameDrain( JSONObject config ) {
        if ( config.getString( "type" ).equals( "bifroest" ) ) {
            String host = config.getString( "host" );
            String[] hostSplit = host.split( "\\." );
            return "bifroest." + hostSplit[1] + "." + hostSplit[0] + "-" + config.getInt("port" ) ;
        } else {
            return inner.nameDrain( config );
        }
    }
    
}
