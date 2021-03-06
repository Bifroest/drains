/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bifroest.names;

import static io.bifroest.names.NameTestUtil.drainWithId;
import static io.bifroest.names.NameTestUtil.drainWithName;
import static io.bifroest.names.NameTestUtil.drainWithType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class TestOverrideWithDrainId {
    
    @Test
    public void testOverrideWithDrainId() {
        NamingScheme subject = new OverrideWithDrainId(new EnumerateDrainByTypeScheme());
        
        assertThat( subject.nameDrain( drainWithType( "buffered" ) ),
                                       is( equalTo( "buffered01" ) ) );
        assertThat( subject.nameDrain( drainWithId( drainWithType( "buffered" ), "foo" ) ),
                                       is( equalTo( "foo" ) ) );
        assertThat( subject.nameDrain( drainWithName( drainWithType( "buffered" ), "foo" ) ),
                                       is( equalTo( "foo" ) ) );
    }
}
