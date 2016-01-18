/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bifroest.names;

import org.json.JSONObject;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class NameTestUtil {
    public static JSONObject drainWithType( String type ) {
        return new JSONObject().put( "type", type );
    }
    
    public static JSONObject drainWithId( JSONObject drain, String drainId ) {
        return drain.put( "drain-id", drainId );
    }
    
    public static JSONObject drainWithName( JSONObject drain, String name ) {
        return drain.put( "name", name );
    }
}
