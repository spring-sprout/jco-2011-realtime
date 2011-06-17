package org.springsprout.realtime.oxquiz.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EntryManager {
    
    private Logger logger = LoggerFactory.getLogger(EntryManager.class);
    private ConcurrentHashMap<String, Entry> entryMap =  new ConcurrentHashMap<String, Entry>();
    
    @Autowired ApplicationContext context;
    
    public void init() {
        entryMap =  new ConcurrentHashMap<String, Entry>();
    }
    
    public Entry entryIn(Entry entry) {
        if(entryMap.containsKey(entry.getId())) {
            return entry;
        }
        logger.info("entry in: {}", entry.getId());
        return entryMap.put(entry.getId(), entry);
    }
    
    public void entryOut(Entry entry) {
        entryOutByEntryId(entry.getId());
    }
    
    public void entryOutByEntryId(String entryId) {
        if(StringUtils.hasText(entryId)) {
            logger.info("entry out by entryId: {}", entryId);
            entryMap.remove(entryId);
        }
    }
    
    public void entryOutBySessionId(String sessionId) {
        if(StringUtils.hasText(sessionId)) {
            for(Entry entry : entryMap.values()) {
                if(sessionId.equals(entry.getSessionId())) {
                    logger.info("entry out by sessionId: {}", entry.getId());
                    entryMap.remove(sessionId);                    
                }
            }
        }
    }
    
    public Entry getEntry(String id) {
        if(!StringUtils.hasText(id)) return null;
        
        if(!entryMap.containsKey(id)) {
            logger.info("not found entry: {}", id);
            return null;
        }
        return entryMap.get(id);
    }
    
    public List<Entry> getAllActiveEntrys() {
        List<Entry> _entrys = new ArrayList<Entry>();
        for(Entry entry : entryMap.values()) {
            if(entry.isActive()) _entrys.add(entry);
        }
        return _entrys;
    }
    
    @Scheduled(cron="*/60 * * * * SUN-SAT")
    public void entryClaner() {
        logger.debug("entryClaner execute!");
        
        for(Entry entry : entryMap.values()) {
            if(!entry.getClient().isConnected()) {
                logger.info("entry[{}] status is disconnected!", entry.getClient().getUid());
                context.publishEvent(new EntryEvent(entry, EntryEventType.OUT));
            }
        }
    }

}
