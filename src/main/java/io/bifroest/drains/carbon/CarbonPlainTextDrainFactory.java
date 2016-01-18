package io.bifroest.drains.carbon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.dns.EndPoint;
import io.bifroest.drains.BasicDrainFactory;

@MetaInfServices
public class CarbonPlainTextDrainFactory<E extends Environment> implements BasicDrainFactory<E> {
    @Override
    public String handledType() {
        return "carbon";
    }

    @Override
    public CarbonPlainTextDrain create( E environment, JSONObject config, String name ) {
        if ( config.has( "carbon host" ) && config.has( "carbon port" ) ) {
            return new CarbonPlainTextDrain( Arrays.asList( EndPoint.of( config.getString( "carbon host" ),
                                                                         config.getInt( "carbon port" ) ) ) );
        } else if ( config.has( "host" ) && config.has( "port" ) ) {
            return new CarbonPlainTextDrain( Arrays.asList( EndPoint.of( config.getString( "host" ),
                                                                         config.getInt( "port" ) ) ) );
        } else if ( config.has( "hosts" ) ) {
            List<EndPoint> endpoints = new ArrayList<>();
            JSONArray hostsArray = config.getJSONArray( "hosts" );
            for ( int i = 0 ; i < hostsArray.length() ; i++ ) {
                JSONObject carbonConfig = hostsArray.getJSONObject( i );

                if ( carbonConfig.has( "host" ) && carbonConfig.has( "port" ) ) {
                    endpoints.add( EndPoint.of( carbonConfig.getString( "host" ), carbonConfig.getInt( "port" ) ) );
                } else {
                    throw new JSONException( "One of your carbon hosts is misconfigured. You need to have "
                            + "keys \"host\" (String) and \"port\" (int) " );
                }
            }
            return new CarbonPlainTextDrain( endpoints );
        } else {
            throw new JSONException( "CarbonPlainTextDrain misconfigured. You need to have "
                    + "EITHER keys \"carbon host\" (String) and \"carbon port\" (int)"
                    + "OR keys \"host\" (String) and \"port\" (int)"
                    + "OR a key hosts (List)" );
        }
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.<Class<? super E>> emptyList();
    }
}
