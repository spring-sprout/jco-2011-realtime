package org.springsprout.realtime.streamhub;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.URL;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.streamhub.api.PushServer;
import com.streamhub.nio.NIOServer;

public class StreamhubServerFactory implements InitializingBean, DisposableBean, FactoryBean<PushServer> {
    
    private InetSocketAddress address;
    private InetSocketAddress streamingAdapterAddress;
    private URL licenseUrl;
    private URL log4jConfigurationUrl;
    
    private PushServer server;
    
    public InetSocketAddress getAddress() {
        return address;
    }
    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getStreamingAdapterAddress() {
        return streamingAdapterAddress;
    }
    public void setStreamingAdapterAddress(InetSocketAddress streamingAdapterAddress) {
        this.streamingAdapterAddress = streamingAdapterAddress;
    }

    public URL getLicenseUrl() {
        return licenseUrl;
    }
    public void setLicenseUrl(URL licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public URL getLog4jConfigurationUrl() {
        return log4jConfigurationUrl;
    }
    public void setLog4jConfigurationUrl(URL log4jConfigurationUrl) {
        this.log4jConfigurationUrl = log4jConfigurationUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(address);
        Assert.notNull(licenseUrl);
        Assert.notNull(log4jConfigurationUrl);
        
        server = new NIOServer(address, streamingAdapterAddress, licenseUrl, log4jConfigurationUrl);
        server.start();
        
        // 접속 종료 이벤트 처리를 5초내에 처리하도록 조정
        Field field = ReflectionUtils.findField(server.getSubscriptionManager().getClass(), "reconnectionTimeoutMillis");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, server.getSubscriptionManager(), new Long(5000));
    }

    @Override
    public void destroy() throws Exception {
        if(this.server != null)
            this.server.stop();
    }
    
    @Override
    public PushServer getObject() throws Exception {
        return this.server;
    }
    
    @Override
    public Class<PushServer> getObjectType() {
        return PushServer.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }

}
