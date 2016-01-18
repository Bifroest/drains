package io.bifroest.dns;

import java.io.IOException;
import java.net.Socket;

public class ResolvedEndPoint {
    private final IPV4Address ip;
    private final int port;

    private ResolvedEndPoint( IPV4Address ip, int port ) {
        this.ip = ip;
        this.port = port;
    }

    public static ResolvedEndPoint of( IPV4Address ip, int port ) {
        return new ResolvedEndPoint( ip, port );
    }

    public IPV4Address getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Socket connect() throws IOException {
        return new Socket( ip.asInetAddress(), port );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( ip == null ) ? 0 : ip.hashCode() );
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
        ResolvedEndPoint other = (ResolvedEndPoint)obj;
        if ( ip == null ) {
            if ( other.ip != null )
                return false;
        } else if ( !ip.equals( other.ip ) )
            return false;
        if ( port != other.port )
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "ResolvedEndPoint [ip=" + ip + ", port=" + port + "]";
    }
}
