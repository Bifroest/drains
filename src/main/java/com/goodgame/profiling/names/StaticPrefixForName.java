package com.goodgame.profiling.names;

import org.json.JSONObject;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class StaticPrefixForName implements NamingScheme {
    private final NamingScheme inner;
    private final String prefix;

    public StaticPrefixForName( String prefix, NamingScheme inner ) {
        this.inner = inner;
        this.prefix = prefix;
    }

    @Override
    public String nameDrain( JSONObject config ) {
        String innerResult = inner.nameDrain( config );
        return prefix + innerResult;
    }
}
