package io.bifroest.drains;

import java.util.Collection;
import java.util.ServiceLoader;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.names.NamingScheme;

public class DrainFactory<E extends Environment> {
    private static final Logger log = LogManager.getLogger();

    @SuppressWarnings( "rawtypes" )
    private static final Collection<BasicDrainFactory> basicFactories = IteratorUtils.toList( ServiceLoader.load( BasicDrainFactory.class ).iterator() );
    @SuppressWarnings( "rawtypes" )
    private static final Collection<DrainWrapperFactory> wrappingFactories = IteratorUtils.toList( ServiceLoader.load( DrainWrapperFactory.class ).iterator() );
    @SuppressWarnings( "rawtypes" )
    private static final Collection<DrainMultipleWrapperFactory> multipleWrappingFactories = IteratorUtils.toList( ServiceLoader.load( DrainMultipleWrapperFactory.class ).iterator() );

    public Drain create( E environment, JSONObject config, NamingScheme scheme ) {
        log.entry( config );
        if ( config.has( "inner" ) ) {
            if ( config.get( "inner" ) instanceof JSONArray ) {
                log.debug( "need to recurse (multiple inners) " + config.getString( "type" ) );
                List<Drain> inners = new ArrayList<>();
                for( int i = 0; i < config.getJSONArray( "inner" ).length(); i++ ) {
                    inners.add( create( environment, config.getJSONArray( "inner" ).getJSONObject( i ), scheme ) );
                }
                DrainMultipleWrapperFactory<E> factory = findMultipleWrappingFactory( config.getString( "type" ) );
                try {
                    return log.exit( factory.wrap( environment, inners, config, scheme.nameDrain( config ) ) );
                } catch ( Exception e ) {
                    try {
                        for( Drain inner : inners ) {
                            if ( inner instanceof Closeable ) {
                                ((Closeable)inner).close();
                            }
                        }
                    } catch ( Exception e2 ) {
                        log.warn( "Things broke even more!", e2 );
                    }
                    throw e;
                }
            } else {
                log.debug( "need to recurse " + config.getString( "type" ) );
                Drain inner = create( environment, config.getJSONObject( "inner" ), scheme );
                DrainWrapperFactory<E> factory = findWrappingFactory( config.getString( "type" ) );
                try {
                    return log.exit( factory.wrap( environment, inner, config, scheme.nameDrain( config ) ) );
                } catch ( Exception e ) {
                    try {
                        if ( inner instanceof Closeable ) {
                            ((Closeable)inner).close();
                        }
                    } catch ( Exception e2 ) {
                        log.warn( "Things broke even more!", e2 );
                    }
                    throw e;
                }
            }
        } else {
            log.debug( "using basic factory" );
            BasicDrainFactory<E> basicFactory = findBasicFactory( config.getString( "type" ) );
            return log.exit( basicFactory.create( environment, config, scheme.nameDrain( config ) ) );
        }
    }

    public void addRequirements( Collection<String> requiredSystems, JSONObject config ) {
        log.entry( config );
        if ( config.has( "inner" ) ) {
            if ( config.get( "inner" ) instanceof JSONArray ) {
                log.debug( "need to recurse (multiple inners) " + config.getString( "type" ) );
                for( int i = 0; i < config.getJSONArray( "inner" ).length(); i++ ) {
                    addRequirements( requiredSystems, config.getJSONArray( "inner" ).getJSONObject( i ) );
                }
                DrainMultipleWrapperFactory<E> factory = findMultipleWrappingFactory( config.getString( "type" ) );
                factory.addRequiredSystems( requiredSystems, config );
            } else {
                log.debug( "need to recurse " + config.getString( "type" ) );
                addRequirements( requiredSystems, config.getJSONObject( "inner" ) );
                DrainWrapperFactory<E> factory = findWrappingFactory( config.getString( "type" ) );
                factory.addRequiredSystems( requiredSystems, config );
            }
        } else {
            log.debug( "using basic factory" );
            BasicDrainFactory<E> basicFactory = findBasicFactory( config.getString( "type" ) );
            basicFactory.addRequiredSystems( requiredSystems, config );
        }
        log.trace( "Resulting required systems: {}",  requiredSystems.toString() ) ;
        log.exit();
    }

    private DrainMultipleWrapperFactory<E> findMultipleWrappingFactory( String type ) {
        log.entry( type );
        for( DrainMultipleWrapperFactory <E> wrappingFactory : multipleWrappingFactories ) {
            log.trace( "considering: {}", wrappingFactory );
            log.trace( "type.equalsIgnoreCase( wrappingFactory.handledType() ) = {}", type.equalsIgnoreCase( wrappingFactory.handledType() ) );
            if ( type.equalsIgnoreCase( wrappingFactory.handledType() ) ) {
                return log.exit( wrappingFactory );
            }
        }
        throw log.throwing( new IllegalArgumentException( "Cannot find factory for type " + type + " with inner class and current environment" ) );
    }

    private DrainWrapperFactory<E> findWrappingFactory( String type ) {
        log.entry( type );
        for( DrainWrapperFactory<E> wrappingFactory : wrappingFactories ) {
            log.trace( "considering: {}", wrappingFactory );
            log.trace( "type.equalsIgnoreCase( wrappingFactory.handledType() ) = {}", type.equalsIgnoreCase( wrappingFactory.handledType() ) );
            if ( type.equalsIgnoreCase( wrappingFactory.handledType() ) ) {
                return log.exit( wrappingFactory );
            }
        }
        throw log.throwing( new IllegalArgumentException( "Cannot find factory for type " + type + " with inner class and current environment" ) );
    }

    private BasicDrainFactory<E> findBasicFactory( String type ) {
        log.entry( type );
        for( BasicDrainFactory<E> basicFactory : basicFactories ) {
            log.trace( "considering: {}", basicFactory );
            log.trace( "type.equalsIgnoreCase( basicFactory.handledType() ) = {}", type.equalsIgnoreCase( basicFactory.handledType() ) );
            if ( type.equalsIgnoreCase( basicFactory.handledType() ) ) {
                return log.exit( basicFactory );
            }
        }
        throw log.throwing( new IllegalArgumentException( "Cannot find factory for type " + type + " with current environment" ) );
    }
}
