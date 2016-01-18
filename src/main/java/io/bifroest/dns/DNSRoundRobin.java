package io.bifroest.dns;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class to facilitate round robin between multiple endpoints.
 * 
 * You enter one or more (hostname,port) or (ip,port) tuples.
 * 
 * This class generates (ip,port) tuples for you in a round robin fashion.
 * 
 * @author sglimm
 */
public class DNSRoundRobin {
    private static final Logger log = LogManager.getLogger();

    private static final Clock clock = Clock.systemUTC();

    private final DNSClient resolver;
    private final List<EndPoint> endpoints;

    private int requestCounter = 0;

    private final Duration cacheLifetime = Duration.ofSeconds( 10 );
    private List<ResolvedEndPoint> resolved;
    private Instant lastUpdate;

    public DNSRoundRobin( DNSClient resolver, List<EndPoint> endpoints ) {
        this.resolver = resolver;
        this.endpoints = new ArrayList<>( endpoints );

        doQuery();
    }

    private void doQuery() {
        resolved = new ArrayList<>(
                endpoints.stream()
                         .flatMap( endpoint -> endpoint.resolveWith( resolver ).stream() )
                         .distinct()
                         .collect( Collectors.toSet() ) );

        log.debug( "Resolved endpoints: {}", resolved );

        lastUpdate = clock.instant();
    }

    private boolean cacheIsTooOld() {
        return Duration.between( lastUpdate, clock.instant() ).compareTo( cacheLifetime ) > 0;
    }

    private void updateCacheIfNecessary() {
        if ( cacheIsTooOld() ) {
            doQuery();
        }
    }

    public ResolvedEndPoint next() {
        updateCacheIfNecessary();
        ResolvedEndPoint ret = resolved.get( requestCounter % resolved.size() );

        requestCounter++;
        if ( requestCounter < 0 ) {
            // The sign of ( a % b ) is the sign of a. We don't want the % above to return negative
            // numbers, so just reset the counter. See JLS 15.17.3
            requestCounter = 0;
        }

        if ( log.isTraceEnabled() ) {
            log.trace( "Next endpoint is {}", ret );
        }
        return ret;
    }
}
