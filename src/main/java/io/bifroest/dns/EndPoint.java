package io.bifroest.dns;

import java.util.Set;
import java.util.stream.Collectors;

public class EndPoint {
    private final Resolveable host;
    private final int port;

    private EndPoint( Resolveable host, int port ) {
        this.host = host;
        this.port = port;
    }

    public static EndPoint of( Resolveable host, int port ) {
        return new EndPoint( host, port );
    }

    public static EndPoint of( String host, int port ) {
        return of( Resolveable.of( host ), port );
    }

    public Resolveable getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Set<ResolvedEndPoint> resolveWith( DNSClient resolver ) {
        return host.resolveWith( resolver )
                   .stream()
                   .map( ip -> ResolvedEndPoint.of( ip, port ) )
                   .collect( Collectors.toSet() );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( host == null ) ? 0 : host.hashCode() );
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        EndPoint other = (EndPoint)obj;
        if ( host == null ) {
            if ( other.host != null )
                return false;
        } else if ( !host.equals( other.host ) )
            return false;
        if ( port != other.port )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EndPoint [host=" + host + ", port=" + port + "]";
    }
}
