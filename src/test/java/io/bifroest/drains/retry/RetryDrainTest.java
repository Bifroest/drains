package io.bifroest.drains.retry;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.junit.Test;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.Drain;

public class RetryDrainTest {
    private Drain inner = mock( Drain.class );

    @SuppressWarnings( "unchecked" )
    private List<Metric> metrics = mock( List.class );

    private static final int RETRY_COUNT = 3;

    @Test
    public void testForwardsOutput() throws IOException {
        @SuppressWarnings( "resource" )
        Drain subject = new RetryDrain( inner, RETRY_COUNT, Duration.ZERO );

        subject.output( metrics );

        verify( inner ).output( metrics );
    }

    @Test
    public void testForwardsFlush() throws IOException {
        @SuppressWarnings( "resource" )
        Drain subject = new RetryDrain( inner, RETRY_COUNT, Duration.ZERO );

        subject.flushRemainingBuffers();

        verify( inner ).flushRemainingBuffers();
    }

    @Test
    public void testForwardsClose() throws IOException {
        Drain subject = new RetryDrain( inner, RETRY_COUNT, Duration.ZERO );

        subject.close();

        verify( inner ).close();
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void testDoesRetry() throws IOException {
        doThrow( new RuntimeException() ).
        doNothing().
        when( inner ).output( any( List.class ) );

        @SuppressWarnings( "resource" )
        Drain subject = new RetryDrain( inner, RETRY_COUNT, Duration.ZERO );

        subject.output( metrics );

        verify( inner, times( 2 ) ).output( metrics );
    }

    @SuppressWarnings( "unchecked" )
    @Test(expected=RetryCountExceededException.class)
    public void testThrowsRetryCountExceededWhenInnerKeepsThrowingException() throws IOException {
        doThrow( new RuntimeException() ).
        when( inner ).output( any( List.class ) );

        @SuppressWarnings( "resource" )
        Drain subject = new RetryDrain( inner, RETRY_COUNT, Duration.ZERO );

        subject.output( metrics );
    }
}
