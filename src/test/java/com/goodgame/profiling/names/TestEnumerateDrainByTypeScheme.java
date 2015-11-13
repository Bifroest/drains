/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodgame.profiling.names;

import static com.goodgame.profiling.names.NameTestUtil.drainWithType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author hkraemer@ggs-hh.net
 */
public class TestEnumerateDrainByTypeScheme {
    @Test
    public void testNamingTheSameType() {
        EnumerateDrainByTypeScheme subject = new EnumerateDrainByTypeScheme();
        assertThat( subject.nameDrain( drainWithType( "buffered" ) ), is( equalTo( "buffered01" ) ) );
        assertThat( subject.nameDrain( drainWithType( "queued" ) ),   is( equalTo( "queued01" ) ) );
        assertThat( subject.nameDrain( drainWithType( "buffered" ) ), is( equalTo( "buffered02" ) ) );
    }

}
