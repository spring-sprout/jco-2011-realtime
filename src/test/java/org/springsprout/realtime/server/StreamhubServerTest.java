package org.springsprout.realtime.server;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*; 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springsprout.realtime.streamhub.StreamhubServerFactory;

import com.streamhub.api.PushServer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StreamhubServerTest {
    
    @Autowired PushServer server;
    
    @Test
    public void 서버가_올바르게_생성되었는가() {
        assertThat(server, is(notNullValue()));
    }
    

}
