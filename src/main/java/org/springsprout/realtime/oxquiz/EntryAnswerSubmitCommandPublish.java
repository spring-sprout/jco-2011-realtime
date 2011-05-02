package org.springsprout.realtime.oxquiz;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springsprout.realtime.streamhub.Publish;

import com.streamhub.api.Client;
import com.streamhub.api.JsonPayload;
import com.streamhub.api.Payload;
import com.streamhub.api.PushServer;

@Component
public class EntryAnswerSubmitCommandPublish implements Publish {
    
    private static final Logger logger = LoggerFactory.getLogger(EntryAnswerSubmitCommandPublish.class);
    
    @Autowired
    private PushServer server;

    @Override
    public String getTopic() {
        return "entryAnswerSubmitCommand";
    }

    @Override
    public void onMessageReceived(Client client, Payload payload) {
        Map<String, String> param = payload.getFields();
        
        String answer = StringUtils.trimToEmpty(param.get("answer"));
        
        if(answer.isEmpty()) {
            logger.info("answer is empty.");
            return;
        }
        
        Payload message = new JsonPayload("notification");
        message.addField("state", "entryAnswerSubmit");
        message.addField("entryId", client.getUid());
        message.addField("answer", answer);
        
        server.publish("notification", message);
    }

}
