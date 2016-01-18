package com.goodgame.profiling.drains.carbon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import io.bifroest.commons.boot.interfaces.Environment;

public class CarbonPlainTextDrainFactoryTest {
    private CarbonPlainTextDrainFactory<Environment> subject;

    @Before
    public void setUp() {
        subject = new CarbonPlainTextDrainFactory<Environment>();
    }

    @Test
    public void testOldConfig() {
        JSONObject config = new JSONObject().put( "carbon host", "somehost" ).put( "carbon port", 12345 );
        subject.create( null, config, "name" );

        // Don't assert anything - just don't throw an exception here!
    }

    @Test
    public void testLessVerboseConfig() {
        JSONObject config = new JSONObject().put( "host", "somehost" ).put( "port", 12345 );
        subject.create( null, config, "name" );

        // Don't assert anything - just don't throw an exception here!
    }

    @Test
    public void testConfigWithMultipleHosts() {
        JSONObject config = new JSONObject().put(
                "hosts", new JSONArray().put(
                        new JSONObject().put( "host", "somehost" ).put( "port", 12345 ) ) );
        subject.create( null, config, "name" );

        // Don't assert anything - just don't throw an exception here!
    }

}
