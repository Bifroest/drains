package com.goodgame.profiling.drains.carbon;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.dns.DNSJavaDNSClient;
import com.goodgame.profiling.dns.DNSRoundRobin;
import com.goodgame.profiling.dns.EndPoint;
import com.goodgame.profiling.drains.AbstractBasicDrain;

public class CarbonPlainTextDrain extends AbstractBasicDrain {
    private final DNSRoundRobin rr;

    public CarbonPlainTextDrain( List<EndPoint> endpoints ) {
        this.rr = new DNSRoundRobin( new DNSJavaDNSClient(),
                                     endpoints );
    }

    @Override
    public void output( List<Metric> metrics ) throws IOException {
        StringBuilder buffer = new StringBuilder();

        for ( Metric metric : metrics ) {
            String line = metric.name() + " " + metric.value() + " " + metric.timestamp() + "\n";
            buffer.append( line );
        }

        try( Socket carbonSocket = rr.next().connect() ) {
            try( Writer toCarbon = new OutputStreamWriter( carbonSocket.getOutputStream() ) ) {
                toCarbon.write( buffer.toString() );
            }
        }
    }
}
