package io.bifroest.names;

import org.json.JSONObject;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class SpecialCarbonDrainNames implements NamingScheme {
    private final NamingScheme inner;

    public SpecialCarbonDrainNames( NamingScheme inner ) {
        this.inner = inner;
    }

    @Override
    public String nameDrain( JSONObject config ) {
        if ( config.getString( "type" ).equals( "carbon" ) ) {
            String host;
            if ( config.has( "carbon host" ) ) {
                host = config.getString( "carbon host" );
            } else if ( config.has( "host" ) ) {
                host = config.getString( "host" );
            } else if ( config.has( "hosts" ) ) {
                return "carbon.multiple-hosts";
            } else {
                return "carbon.misconfigured";
            }
            String[] hostSplit = host.split( "\\." );
            return "carbon." + hostSplit[1] + "." + hostSplit[0] + "-" + config.getInt("port" ) ;
        } else {
            return inner.nameDrain( config );
        }
    }
}
