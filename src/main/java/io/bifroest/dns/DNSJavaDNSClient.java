package io.bifroest.dns;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DNSJavaDNSClient implements DNSClient {
    private static final Logger log = LogManager.getLogger();

    @Override
    public Set<IPV4Address> resolve( String hostname ) {
        Lookup lookup;
        try {
            lookup = new Lookup( hostname );
        } catch( TextParseException e ) {
            log.error( hostname + " is not a valid hostname!" );
            return Collections.emptySet();
        }

        Record[] records = lookup.run();
        if ( records == null ) {
            return Collections.emptySet();
        }

        Set<IPV4Address> ret = Arrays.stream( records )
                                     .filter( record -> record.getType() == Type.A ) // This _should_ always be true...
                                     .map( record -> (ARecord)record )
                                     .map( arecord -> arecord.getAddress() )
                                     .map( inetAddress -> IPV4Address.fromInetAddress( inetAddress ) )
                                     .collect( Collectors.toSet() );
        if ( log.isTraceEnabled() ) {
            log.trace( "Resolving {} to {}", hostname, ret );
        }
        return ret;
    }
}
