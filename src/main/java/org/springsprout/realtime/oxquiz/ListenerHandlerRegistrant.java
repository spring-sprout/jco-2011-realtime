package org.springsprout.realtime.oxquiz;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springsprout.realtime.streamhub.Publish;
import org.springsprout.realtime.streamhub.Subscription;

import com.streamhub.api.Client;
import com.streamhub.api.Payload;
import com.streamhub.api.PublishListener;
import com.streamhub.api.PushServer;
import com.streamhub.api.SubscriptionListener;

public class ListenerHandlerRegistrant implements InitializingBean {
    
    private Logger logger = LoggerFactory.getLogger(ListenerHandlerRegistrant.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private PushServer server;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Subscription> subscriptions = applicationContext.getBeansOfType(Subscription.class);
        if(subscriptions != null) {
            for(Subscription subscription : subscriptions.values()) {
                if(StringUtils.isBlank(subscription.getTopic()))
                    throw new IllegalArgumentException("topic is blank: " + subscription.getClass().getSimpleName());
                
                logger.info("registering subscription: {} - {}", new Object[]{subscription.getTopic(), subscription.getClass().getSimpleName()});
                
                server.getSubscriptionManager().addSubscriptionListener(
                        new SubscriptionListenerHandler(subscription.getTopic(), subscription));
            }
        }
        
        Map<String, Publish> publishs = applicationContext.getBeansOfType(Publish.class);
        if(publishs != null) {
            for(Publish publish : publishs.values()) {
                if(StringUtils.isBlank(publish.getTopic()))
                    throw new IllegalArgumentException("topic is blank: " + publish.getClass().getSimpleName());
                
                logger.info("registering publish: {} - {}", new Object[]{publish.getTopic(), publish.getClass().getSimpleName()});
                
                server.getSubscriptionManager().addPublishListener(
                        new PublishListenerHandler(publish.getTopic(), publish));
            }
        }
    }
    
    private class SubscriptionListenerHandler implements SubscriptionListener {
        
        private Logger logger = LoggerFactory.getLogger(SubscriptionListenerHandler.class);
        
        private final String listenerTopic;
        private final Subscription subscription;
        
        public SubscriptionListenerHandler(String topic, Subscription subscription) {
            this.listenerTopic = topic;
            this.subscription = subscription;
        }

        @Override
        public void onSubscribe(String topic, Client client) {
            if(listenerTopic.equals(topic)) {
                logger.info("onSubscribe: {}", topic);
                
                subscription.onSubscribe(client);
            }
        }

        @Override
        public void onUnSubscribe(String topic, Client client) {
            if(listenerTopic.equals(topic)) {
                logger.info("onSubscribe: {}", topic);
                
                subscription.onUnSubscribe(client);
            }
        }
        
    }
    
    private class PublishListenerHandler implements PublishListener {
        
        private Logger logger = LoggerFactory.getLogger(PublishListenerHandler.class);
        
        private final String listenerTopic;
        private final Publish publish;
        
        public PublishListenerHandler(String topic, Publish publish) {
            this.listenerTopic = topic;
            this.publish = publish;
        }

        @Override
        public void onMessageReceived(Client client, String topic, Payload payload) {
            if(listenerTopic.equals(topic)) {
                logger.info("onMessageReceived: {}", topic);
                
                publish.onMessageReceived(client, payload);
            }
        }
    }

}
