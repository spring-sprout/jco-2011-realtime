/**
  * ©2011 Looah, LLC
  * jco-2011-realtime
  * created by isyoon Jun 5, 2011 2:22:21 PM
  */
package org.springsprout.realtime.chunked;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class ChatStore {
    
    private ConcurrentHashMap<String, LinkedList<HashMap<String,String>>> store = new ConcurrentHashMap<String, LinkedList<HashMap<String,String>>>();
    private HashMap<String,String> nonMap = new HashMap<String,String>();

    @PostConstruct
    public void postConstruct(){
        nonMap.put("state","wait");
    }
    
    public Boolean putMessage(String uName,String message){
        if(uName == null || message == null){
            return false;
        }
        HashMap<String,String> msg = new HashMap<String,String>();
        msg.put("name", uName);
        msg.put("msg", message);
        msg.put("state", "msg");
        for(String key : store.keySet()){
            store.get(key).add(msg);
        }
        return true;
    }
    

    public HashMap<String,String> getData(String uuid) {
        if(store.get(uuid) == null){
            enterStore(uuid);
            return nonMap;
        }
        HashMap<String,String> returnMap = new HashMap<String,String>();
        if(!store.get(uuid).isEmpty()){
            returnMap = store.get(uuid).poll();
        }
        if(returnMap.isEmpty()){
            return nonMap;
        }
        return returnMap;
        
    }
    
    public void enterStore(String uuid){
        if(store.get(uuid) == null){
            store.put(uuid, new LinkedList<HashMap<String,String>>());
        }
    }

    public void remveStore(String uuid) {
        if(store.get(uuid) != null){
            store.remove(uuid);
        }
    }
}
