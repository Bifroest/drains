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
public class TestBifroestDrainName {
    @Test
    public void testNameGeneration() {
        NamingScheme subject = new SpecialBifroestDrainNames( new EnumerateDrainByTypeScheme() );
        JSONObject config = drainWithType( "bifroest" ).put( "host", "my.bifroest.host" ).put( "port", 5102 );
        
        assertThat( subject.nameDrain( config ),
                    is( equalTo( "bifroest.bifroest.my-5102") ) );
    }
}
