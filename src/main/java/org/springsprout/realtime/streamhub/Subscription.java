package org.springsprout.realtime.streamhub;

import com.streamhub.api.Client;

public interface Subscription extends StreamhubListener {
    
    public void onSubscribe(Client client);

    public void onUnSubscribe(Client client);
    
}
