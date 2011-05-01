package org.springsprout.realtime.server;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.streamhub.api.PushServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StreamhubServerTest {
    
    @Autowired PushServer server;
    
    @Test
    public void 서버가_올바르게_생성되었는가() {
        assertThat(server, is(notNullValue()));
    }
    
    @After
    public void 서버_죽이기() throws Exception{
        server.stop();
    }
}
