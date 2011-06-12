package org.springsprout.realtime.oxquiz.entry;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.streamhub.api.Client;

public class Entry {

    private Client client;
    private String sessionId;
    
    public Entry(Client client) {
        Assert.notNull(client);
        
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getId() {
        return client.getUid();
    }
    public boolean isActive() {
        return StringUtils.hasText(sessionId);
    }
    
}
