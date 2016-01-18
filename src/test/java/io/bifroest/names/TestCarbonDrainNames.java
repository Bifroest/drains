/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bifroest.names;

import static io.bifroest.names.NameTestUtil.drainWithType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.json.JSONObject;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class TestCarbonDrainNames {
    @Test
    public void testSingleCarbon() {
        NamingScheme subject = new SpecialCarbonDrainNames( new EnumerateDrainByTypeScheme() );
        JSONObject config = drainWithType( "carbon" ).put( "host", "cassandra01.bifroest.aws-euw.ggs-net.com" ).put( "port", 9003 );

        assertThat( subject.nameDrain( config ),
                    is( equalTo( "carbon.bifroest.cassandra01-9003" ) ) );
    }

    @Test
    public void testSingleCarbonOldConfig() {
        NamingScheme subject = new SpecialCarbonDrainNames( new EnumerateDrainByTypeScheme() );
        JSONObject config = drainWithType( "carbon" ).put( "carbon host", "cassandra01.bifroest.aws-euw.ggs-net.com" ).put( "port", 9003 );

        assertThat( subject.nameDrain( config ),
                    is( equalTo( "carbon.bifroest.cassandra01-9003" ) ) );
    }

    @Test
    public void testMultipleCarbons() {
        NamingScheme subject = new SpecialCarbonDrainNames( new EnumerateDrainByTypeScheme() );
        JSONObject config = drainWithType( "carbon" ).put( "hosts", new JSONObject().put( "host", "cassandra01.bifroest.aws-euw.ggs-net.com" ).put( "port", 9003 ) );

        assertThat( subject.nameDrain( config ),
                    is( equalTo( "carbon.multiple-hosts" ) ) );
    }

    @Test
    public void testInvalidConfig() {
        NamingScheme subject = new SpecialCarbonDrainNames( new EnumerateDrainByTypeScheme() );
        JSONObject config = drainWithType( "carbon" );

        assertThat( subject.nameDrain( config ),
                    is( equalTo( "carbon.misconfigured" ) ) );
    }
}
