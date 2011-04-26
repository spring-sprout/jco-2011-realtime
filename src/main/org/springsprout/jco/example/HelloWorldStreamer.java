package org.springsprout.jco.example;

import com.streamhub.api.Client;
import com.streamhub.api.JsonPayload;
import com.streamhub.api.PushServer;
import com.streamhub.api.SubscriptionListener;
import com.streamhub.nio.NIOServer;

public class HelloWorldStreamer implements SubscriptionListener {

	public HelloWorldStreamer() throws Exception {
	  // Create a new server on port 7878
	  PushServer server = new NIOServer(8070);
	  server.start();
	  server.getSubscriptionManager().addSubscriptionListener(this);
	}

	public void onSubscribe(String topic, Client client) {
		JsonPayload payload = new JsonPayload(topic);
		payload.addField("Response", "Hello World!");
		client.send(topic, payload);
	}

	public void onUnSubscribe(String topic, Client client) {

	}
}
