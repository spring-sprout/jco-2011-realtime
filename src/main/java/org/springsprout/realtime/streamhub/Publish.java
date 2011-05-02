package org.springsprout.realtime.streamhub;

import com.streamhub.api.Client;
import com.streamhub.api.Payload;

public interface Publish extends StreamhubListener {

    public void onMessageReceived(Client client, Payload payload);
    
}
