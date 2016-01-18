package io.bifroest.dns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DNSRoundRobinTest {
    private DNSClient dns = mock(DNSClient.class);

    private String hostname = "foo";
    private String hostname2 = "bar";
    private IPV4Address ip = IPV4Address.fromBytes( 10, 1, 2, 3 );
    private IPV4Address ip2 = IPV4Address.fromBytes( 10, 1, 2, 4 );
    private IPV4Address ip3 = IPV4Address.fromBytes( 10, 1, 2, 5 );
    private int port = 12345;
    private int port2 = 12346;

    @Test
    public void testOneHostnameResolvesToOneIP() {
        when( dns.resolve( hostname ) ).thenReturn( new HashSet<>( Arrays.asList( ip ) ) );

        DNSRoundRobin subject = new DNSRoundRobin( dns,
                                                   Arrays.asList( EndPoint.of( hostname, port ) ) );

        assertEquals( subject.next(), ResolvedEndPoint.of( ip, port ) );
    }

    @Test
    public void testOneIpResolvesToOneIP() {
        DNSRoundRobin subject = new DNSRoundRobin( dns,
                                                   Arrays.asList( EndPoint.of( ip2, port ) ) );

        assertEquals( subject.next(), ResolvedEndPoint.of( ip2, port ) );
    }

    @Test
    public void testOneHostnameResolvesToMultipleIPs() {
        when( dns.resolve( hostname ) ).thenReturn( new HashSet<>( Arrays.asList( ip, ip2 ) ) );

        DNSRoundRobin subject = new DNSRoundRobin( dns,
                                                   Arrays.asList( EndPoint.of( hostname, port ) ) );

        Set<ResolvedEndPoint> got = new HashSet<>();
        got.add( subject.next() );
        got.add( subject.next() );

        Set<ResolvedEndPoint> expected = new HashSet<>();
        expected.add( ResolvedEndPoint.of( ip, port ) );
        expected.add( ResolvedEndPoint.of( ip2, port ) );

        // Test that we loop through all expected elements...
        assertEquals( expected, got );

        // ... before returning the same element a second time.
        assertTrue( expected.contains( subject.next() ) );
    }

    @Test
    public void testMultipleHostnamesWithOverlappingIPs() {
        when( dns.resolve( hostname ) ).thenReturn( new HashSet<>( Arrays.asList( ip, ip2 ) ) );
        when( dns.resolve( hostname2 ) ).thenReturn( new HashSet<>( Arrays.asList( ip2, ip3 ) ) );

        DNSRoundRobin subject = new DNSRoundRobin( dns,
                                                   Arrays.asList( EndPoint.of( hostname, port ),
                                                                  EndPoint.of( hostname2, port ) ) );

        Set<ResolvedEndPoint> got = new HashSet<>();
        got.add( subject.next() );
        got.add( subject.next() );
        got.add( subject.next() );

        Set<ResolvedEndPoint> expected = new HashSet<>();
        expected.add( ResolvedEndPoint.of( ip, port ) );
        expected.add( ResolvedEndPoint.of( ip2, port ) );
        expected.add( ResolvedEndPoint.of( ip3, port ) );

        // Test that we loop through all expected elements...
        assertEquals( expected, got );

        // ... before returning the same element a second time.
        assertTrue( expected.contains( subject.next() ) );
    }

    @Test
    public void testMultipleHostnamesWithOverlappingIPsAndDifferentPorts() {
        when( dns.resolve( hostname ) ).thenReturn( new HashSet<>( Arrays.asList( ip, ip2 ) ) );
        when( dns.resolve( hostname2 ) ).thenReturn( new HashSet<>( Arrays.asList( ip2, ip3 ) ) );

        DNSRoundRobin subject = new DNSRoundRobin( dns,
                Arrays.asList( EndPoint.of( hostname, port ),
                               EndPoint.of( hostname2, port2 ) ) );

        Set<ResolvedEndPoint> got = new HashSet<>();
        got.add( subject.next() );
        got.add( subject.next() );
        got.add( subject.next() );
        got.add( subject.next() );

        Set<ResolvedEndPoint> expected = new HashSet<>();
        expected.add( ResolvedEndPoint.of( ip, port ) );
        expected.add( ResolvedEndPoint.of( ip2, port ) );
        expected.add( ResolvedEndPoint.of( ip2, port2 ) );
        expected.add( ResolvedEndPoint.of( ip3, port2 ) );

        // Test that we loop through all expected elements...
        assertEquals( expected, got );

        // ... before returning the same element a second time.
        assertTrue( expected.contains( subject.next() ) );
    }
}
