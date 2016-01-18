package io.bifroest.dns;

import java.util.Set;

public interface Resolveable {
    Set<IPV4Address> resolveWith( DNSClient client );

    static Resolveable of( String name ) {
        if ( IPV4Address.isValidIPString( name ) ) {
            return IPV4Address.fromString( name );
        } else {
            return Domainname.fromString( name );
        }
    }
}
