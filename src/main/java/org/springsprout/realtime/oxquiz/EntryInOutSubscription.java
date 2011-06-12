package org.springsprout.realtime.oxquiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springsprout.realtime.oxquiz.entry.Entry;
import org.springsprout.realtime.oxquiz.entry.EntryEvent;
import org.springsprout.realtime.oxquiz.entry.EntryEventType;
import org.springsprout.realtime.oxquiz.entry.EntryManager;
import org.springsprout.realtime.streamhub.Subscription;

import com.streamhub.api.Client;
import com.streamhub.api.JsonPayload;
import com.streamhub.api.Payload;
import com.streamhub.api.PushServer;

@Component
public class EntryInOutSubscription implements Subscription, ApplicationListener<EntryEvent> {

    private Logger logger = LoggerFactory.getLogger(EntryInOutSubscription.class);
    
    @Autowired
    private PushServer server;
    
    @Autowired
    private EntryManager entryManager;

    @Override
    public String getTopic() {
        return "entry";
    }
    
    @Override
    public void onSubscribe(Client client) {
        // 새로운 참가자가 등장.
        entryManager.entryIn(new Entry(client));
        
        // 요청한 참가자에게 부여된 아이디 전송
        Payload notification = new JsonPayload("notification");
        notification.addField("state", "myEntryId");
        notification.addField("entryId", client.getUid());
        client.send("notification", notification);
        
        logger.info("onSubscribe client: {}", client.getUid());        
    }

    @Override
    public void onUnSubscribe(Client client) {
        Payload entry = new JsonPayload(getTopic());
        entry.addField("state", "entryOut");
        entry.addField("entryId", client.getUid());
        
        server.publish(getTopic(), entry);
        
        // 참가자가 퀴즈방에서 나감.
        entryManager.entryOut(client.getUid());
        
        logger.info("onUnSubscribe client: {}", client.getUid());
        
    }

    /**
     * 참가자 활성화 이벤트 처리
     */
    @Override
    public void onApplicationEvent(EntryEvent event) {
        Client client = ((Entry) event.getSource()).getClient();
        
        logger.info("onApplicationEvent cliendId: {} - eventType: {}", new Object[]{client.getUid(), event.getEventType()});
        
        if(EntryEventType.ACTIVE.equals(event.getEventType())) {
            // 참가자가 활성화되면 현재 접속중이 모든 사람에게 참가자 정보를 전송
            
            // 접속한 모든 참가자에게 새로운 참가자 정보를 게재
            Payload entry = new JsonPayload(getTopic());
            entry.addField("state", "entryIn");
            entry.addField("entryId", client.getUid());
            server.publish(getTopic(), entry);
            
            logger.info("entry({}) in event processing!", client.getUid());
        } else if(EntryEventType.REFRESH.equals(event.getEventType())) {
            // 새로고침 이벤트가 들어오면 기존에 유지되던 커넥션은 끊어버려!
            
            // onUnSubscribe(client);
            client.disconnect();
            
            logger.info("entry({}) refresh event processing!", client.getUid());
        }
    }

}
