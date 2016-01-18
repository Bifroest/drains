/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bifroest.names;

import static io.bifroest.names.NameTestUtil.drainWithType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class TestPersistentDrainNames {
    @Test
    public void testPersistentDrainNames() {
        NamingScheme subject = new PersistentDrainNames( new EnumerateDrainByTypeScheme() );
        assertThat( subject.nameDrain( drainWithType( "persistent" ).put( "id", "cassandra" ) ),
                    is(equalTo( "persistent-cassandra" ) ) );
    }
}
