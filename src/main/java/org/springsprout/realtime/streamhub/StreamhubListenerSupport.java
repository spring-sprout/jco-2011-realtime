package org.springsprout.realtime.streamhub;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.streamhub.api.PublishListener;
import com.streamhub.api.PushServer;
import com.streamhub.api.SubscriptionListener;

public abstract class StreamhubListenerSupport implements SubscriptionListener, PublishListener, InitializingBean {
    
    @Autowired
    private PushServer server;

    public PushServer getServer() {
        return server;
    }
    public void setServer(PushServer server) {
        this.server = server;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        server.getSubscriptionManager().addSubscriptionListener(this);
        server.getSubscriptionManager().addPublishListener(this);
    }    

}
