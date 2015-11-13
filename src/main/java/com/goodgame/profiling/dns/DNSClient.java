package com.goodgame.profiling.dns;

import java.util.Set;

public interface DNSClient {
    public Set<IPV4Address> resolve( String hostname );
}
