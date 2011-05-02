package org.springsprout.realtime.oxquiz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.streamhub.api.Client;

@Component
public class EntryStore {
    
    private static final Logger logger = LoggerFactory.getLogger(EntryInOutSubscription.class);
    private static ConcurrentHashMap<String, Client> entrys = new ConcurrentHashMap<String, Client>();
    
    public Client rememberEntry(Client client) {
        if(entrys.containsKey(client.getUid())) {
            return client;
        }
        logger.info("remember entry uid: {}", client.getUid());
        return entrys.put(client.getUid(), client);
    }
    
    public void forgetEntry(Client client) {
        logger.info("forget entry uid: {}", client.getUid());
        entrys.remove(client.getUid());
    }
    
    public Client getEntry(String uid) {
        if(!entrys.containsKey(uid)) {
            logger.info("not found entry uid: {}", uid);
            throw new IllegalArgumentException("not found entry uid: " + uid);
        }
        
        return entrys.get(uid);
    }
    
    public List<Client> getAllEntry() {
        return new ArrayList<Client>(entrys.values());
    }

}
