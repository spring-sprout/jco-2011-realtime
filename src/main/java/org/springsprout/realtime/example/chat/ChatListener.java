package org.springsprout.realtime.example.chat;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.streamhub.api.Client;
import com.streamhub.api.JsonPayload;
import com.streamhub.api.Payload;
import com.streamhub.api.PublishListener;
import com.streamhub.api.PushServer;
import com.streamhub.api.SubscriptionListener;

@Component
public class ChatListener implements SubscriptionListener, PublishListener, InitializingBean {

    private static final String CHAT_ROOM_NAME = "ChatRoom";
    private static final Logger logger = LoggerFactory.getLogger(ChatListener.class);
    
    @Autowired
    private PushServer server;

    private Map<Client, String> userNameMappings = new HashMap<Client, String>();

    public void onSubscribe(String topic, Client client) {
        if (CHAT_ROOM_NAME.equals(topic)) {
            sendEnteredChatNotification(topic, client);
        }
    }

    public void onUnSubscribe(String topic, Client client) {
        if (CHAT_ROOM_NAME.equals(topic)) {
            sendLeftChatNotification(topic, client);
        }
    }

    public void onMessageReceived(Client client, String topic, Payload payload) {
        if (CHAT_ROOM_NAME.equals(topic)) {
            Map<String, String> fields = payload.getFields();
            if (fields.containsKey("user")) {
                String newUserName = fields.get("user");
                sendUserNameChangeNotification(topic, client, newUserName);
                userNameMappings.put(client, newUserName);
            } else if (fields.containsKey("chat")) {
                sendChatMessage(topic, client, fields);
            }
        }
    }

    private void sendChatMessage(String topic, Client client, Map<String, String> fields) {
        if (fields.containsKey("chat")) {
            Payload chatMessage = new JsonPayload(topic);
            
            String username = "User " + client.getUid();
            if (userNameMappings.containsKey(client))
                username = userNameMappings.get(client);
                
            chatMessage.addField("user", username);
            chatMessage.addField("chat", fields.get("chat"));
            
            server.publish(topic, chatMessage);
        } else {
            logger.error("Incoming payload did not contain chat message, full message: " + fields.toString());
        }
    }

    private void sendEnteredChatNotification(String topic, Client client) {
        sendNotification(topic, "User " + client.getUid() + " entered chat");
    }

    private void sendLeftChatNotification(String topic, Client client) {
        sendNotification(topic, "User " + client.getUid() + " left chat");
    }

    private void sendUserNameChangeNotification(String topic, Client client, String newUserName) {
        String oldUserName = "User " + client.getUid();
        if (userNameMappings.containsKey(client)) {
            oldUserName = userNameMappings.get(client);
        }
        sendNotification(topic, oldUserName + " changed their name to " + newUserName);
    }

    private void sendNotification(String topic, String notificationMessage) {
        Payload notification = new JsonPayload(topic);
        notification.addField("notification", notificationMessage);
        server.publish(topic, notification);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        server.getSubscriptionManager().addSubscriptionListener(this);
        server.getSubscriptionManager().addPublishListener(this);
    }
    
}
