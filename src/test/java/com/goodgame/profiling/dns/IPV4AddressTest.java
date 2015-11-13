package com.goodgame.profiling.dns;

import static org.junit.Assert.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import org.junit.Test;

public class IPV4AddressTest {
    @Test
    public void testFactoriesDoTheSameThing() {
        IPV4Address a = IPV4Address.fromByteArray( new byte[] { 127, 0, 0, 1 } );
        IPV4Address b = IPV4Address.fromBytes( 127, 0, 0, 1 );
        IPV4Address c = IPV4Address.fromString( "127.0.0.1" );

        assertTrue( a.equals( b ) );
        assertTrue( a.equals( c ) );
    }

    @Test
    public void testFromInet4Address() throws UnknownHostException {
        IPV4Address a = IPV4Address.fromBytes( 127, 0, 0, 1 );
        IPV4Address b = IPV4Address.fromInetAddress( Inet4Address.getByAddress( new byte[] { 127, 0, 0, 1 } ) );

        assertEquals( a, b );
    }

    @Test
    public void testAsByteArray() {
        IPV4Address a = IPV4Address.fromBytes( 127, 0, 0, 1 );

        assertArrayEquals( new byte[] { 127, 0, 0, 1 }, a.asByteArray() );
    }

    @Test
    public void testAsString() {
        IPV4Address a = IPV4Address.fromBytes( 127, 0, 0, 1 );

        assertEquals( "127.0.0.1", a.asString() );
    }

    @Test
    public void testAsStringWithHighBitSet() {
        IPV4Address a = IPV4Address.fromBytes( 129, 0, 255, 1 );

        assertEquals( "129.0.255.1", a.asString() );
    }

    @Test
    public void testAsInetAddr() throws UnknownHostException {
        IPV4Address a = IPV4Address.fromBytes( 127, 0, 0, 1 );

        assertEquals( Inet4Address.getByAddress( new byte[] { 127, 0, 0, 1 } ), a.asInetAddress() );
    }

    @Test
    public void testIsValidIPStringOnValidIP() {
        assertTrue( IPV4Address.isValidIPString( "1.2.3.4" ) );
    }

    @Test
    public void testIsValidIPStringOnWrongNumberOfDots() {
        assertFalse( IPV4Address.isValidIPString( "1.2.3.4.5" ) );
    }

    @Test
    public void testIsValidIPStringOnOutOfRangeNumber() {
        assertFalse( IPV4Address.isValidIPString( "321.2.3.4" ) );
    }

    @Test
    public void testIsValidIPStringOnHostnameThatLooksLikeAnIP() {
        assertFalse( IPV4Address.isValidIPString( "a.b.c.d" ) );
    }
}
