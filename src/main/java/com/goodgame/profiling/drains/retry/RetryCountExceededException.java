package com.goodgame.profiling.drains.retry;

import java.util.List;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

public class RetryCountExceededException extends RuntimeException {
    private final Bag<String> previousMessages;

    public RetryCountExceededException( List<Throwable> previousExceptions ) {
        previousMessages = new HashBag<>();

        for ( Throwable t : previousExceptions ) {
            previousMessages.add( t.toString() );
        }
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder( "Retry count exceeded, giving up. Exceptions thrown were:" );

        for ( String message : previousMessages.uniqueSet() ) {
            sb.append( "\n" + previousMessages.getCount( message ) + " times: " + message );
        }

        return sb.toString();
    }
}
