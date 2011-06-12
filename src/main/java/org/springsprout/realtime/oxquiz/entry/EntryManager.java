package org.springsprout.realtime.oxquiz.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EntryManager {
    
    private Logger logger = LoggerFactory.getLogger(EntryManager.class);
    private ConcurrentHashMap<String, Entry> entryMap =  new ConcurrentHashMap<String, Entry>();
    
    public Entry entryIn(Entry entry) {
        if(entryMap.containsKey(entry.getId())) {
            return entry;
        }
        logger.info("entry in: {}", entry.getId());
        return entryMap.put(entry.getId(), entry);
    }
    
    public void forgetEntry(Entry entry) {
        entryOut(entry.getId());
    }
    
    public void entryOut(String entryId) {
        if(StringUtils.hasText(entryId)) {
            logger.info("entry out: {}", entryId);
            entryMap.remove(entryId);
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

}
