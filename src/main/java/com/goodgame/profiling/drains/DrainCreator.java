package com.goodgame.profiling.drains;

import com.goodgame.profiling.commons.logging.LogService;
import org.json.JSONObject;

import com.goodgame.profiling.commons.systems.configuration.EnvironmentWithJSONConfiguration;
import com.goodgame.profiling.names.EnumerateDrainByTypeScheme;
import com.goodgame.profiling.names.NamingScheme;
import com.goodgame.profiling.names.OverrideWithDrainId;
import org.slf4j.ext.XLogger;

public class DrainCreator< E extends EnvironmentWithJSONConfiguration > {
    public static final XLogger logger = LogService.getXLogger(DrainCreator.class);

    private static final NamingScheme DEFAULT_NAMING_SCHEME = new OverrideWithDrainId( new EnumerateDrainByTypeScheme() );

    public Drain loadFromDrainConfiguration( E environment, JSONObject drainConfig, NamingScheme namingScheme ) {
        logger.debug( drainConfig.toString() );

        return new DrainFactory<E>().create( environment, drainConfig, namingScheme );
    }

    public Drain loadFromDrainConfiguration( E environment, JSONObject drainConfig ) {
        return loadFromDrainConfiguration( environment, drainConfig, DEFAULT_NAMING_SCHEME );
    }

    public Drain loadConfiguration( E environment, JSONObject config, NamingScheme namingScheme ) {
        return loadFromDrainConfiguration( environment, config.getJSONObject( "drain" ), namingScheme );
    }

    public Drain loadConfiguration( E environment, JSONObject config ) {
        return loadConfiguration( environment, config, DEFAULT_NAMING_SCHEME );
    }
}
