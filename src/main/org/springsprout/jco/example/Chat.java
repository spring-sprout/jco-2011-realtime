package org.springsprout.jco.example;

import java.io.File;

import com.streamhub.api.Client;
import com.streamhub.api.JsonPayload;
import com.streamhub.api.Payload;
import com.streamhub.api.PublishListener;
import com.streamhub.api.PushServer;
import com.streamhub.nio.NIOServer;

public class Chat implements PublishListener {
	private PushServer server;
	
	public Chat() throws Exception {
		server = new NIOServer(7878);
		server.addStaticContent(new File("."));
		server.start();
		server.getSubscriptionManager().addPublishListener(this);
	}

	@Override
	public void onMessageReceived(Client client, String topic, Payload payload) {
		Payload chatMessage = new JsonPayload(topic);
		chatMessage.addField("client", "Client-" + client.getUid());
		chatMessage.addField("message", payload.getFields().get("message"));
		server.publish(topic, chatMessage);
	}
}
