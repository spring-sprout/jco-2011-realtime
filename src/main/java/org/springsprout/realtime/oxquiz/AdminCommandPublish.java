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
public class AdminCommandPublish implements Publish {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminCommandPublish.class);
    
    @Autowired
    private PushServer server;

    @Override
    public String getTopic() {
        return "adminCommand";
    }

    @Override
    public void onMessageReceived(Client client, Payload payload) {
        Map<String, String> param = payload.getFields();
        
        String command = StringUtils.trimToEmpty(param.get("command"));
        
        if(command.isEmpty()) {
            logger.info("command is empty.");
            return;
        }
        
        Payload message = new JsonPayload("notification");
        
        if("currentQuizClose".equals(command)) {
            message.addField("state", "currentQuizClose");    
        } else if("nextQuiz".equals(command)) {
            message.addField("state", "nextQuiz");
            message.addField("quiz", QuizStore.next());
        }
        
        server.publish("notification", message);
    }

}
