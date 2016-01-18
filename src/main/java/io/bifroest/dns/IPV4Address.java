package io.bifroest.dns;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * IMHO, InetAddress does WAY too much. e.g. name resolution. So here is a way simpler class.
 * 
 * @author sglimm
 */
public class IPV4Address implements Resolveable {
    private byte[] addr;

    private IPV4Address( byte[] bytes ) {
        Objects.requireNonNull( bytes );
        if ( bytes.length != 4 ) {
            throw new IllegalArgumentException( "bytes[] must have exactly 4 elements" );
        }

        this.addr = Arrays.copyOf( bytes, bytes.length );
    }

    public static IPV4Address fromByteArray( byte[] bytes ) {
        return new IPV4Address( bytes );
    }

    public static IPV4Address fromBytes( int...bytes ) {
        Objects.requireNonNull( bytes );
        if ( bytes.length != 4 ) {
            throw new IllegalArgumentException( "bytes[] must have exactly 4 elements" );
        }
        for ( int i = 0 ; i < bytes.length ; i++ ) {
            if ( ! ( bytes[i] >= 0 && bytes[i] <= 255 ) ) {
                throw new IllegalArgumentException( "All bytes must be between 0 and 255" );
            }
        }

        return new IPV4Address( new byte[] { (byte)bytes[0],
                                             (byte)bytes[1],
                                             (byte)bytes[2],
                                             (byte)bytes[3]
        } ) ;
    }

    public static IPV4Address fromString( String ip ) {
        String[] splitted = StringUtils.split( ip, '.' );
        byte[] bytes = new byte[splitted.length];
        for ( int i = 0 ; i < splitted.length ; i++ ) {
            bytes[i] = Byte.parseByte( splitted[i] );
        }
        return new IPV4Address( bytes );
    }

    public static boolean isValidIPString( String ip ) {
        String[] splitted = StringUtils.split( ip, '.' );
        if ( splitted.length != 4 ) {
            return false;
        }

        for ( int i = 0 ; i < splitted.length ; i++ ) {
            int b;
            try {
                b = Integer.parseInt( splitted[i] );
            } catch( NumberFormatException e ) {
                return false;
            }
            if ( ! ( b >= 0 && b <= 255) ) {
                return false;
            }
        }
        return true;
    }

    public static IPV4Address fromInetAddress( InetAddress addr ) {
        return new IPV4Address( addr.getAddress() );
    }

    public byte[] asByteArray() {
        return Arrays.copyOf( addr, 4 );
    }

    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append( addr[0] < 0 ? addr[0] + 256 : addr[0] );
        sb.append( '.' );
        sb.append( addr[1] < 0 ? addr[1] + 256 : addr[1] );
        sb.append( '.' );
        sb.append( addr[2] < 0 ? addr[2] + 256 : addr[2] );
        sb.append( '.' );
        sb.append( addr[3] < 0 ? addr[3] + 256 : addr[3] );
        return sb.toString();
    }

    public InetAddress asInetAddress() throws UnknownHostException {
        return Inet4Address.getByAddress( addr );
    }


    @Override
    public Set<IPV4Address> resolveWith( DNSClient client ) {
        HashSet<IPV4Address> result = new HashSet<>();
        result.add( this );
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode( addr );
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
        IPV4Address other = (IPV4Address)obj;
        if ( !Arrays.equals( addr, other.addr ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "IPV4Address " + asString() + "]";
    }
}
