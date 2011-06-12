package org.springsprout.realtime.oxquiz.entry;

import org.springframework.context.ApplicationEvent;

public class EntryEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private EntryEventType eventType;
    
    public EntryEvent(Entry entry, EntryEventType eventType) {
        super(entry);
        this.eventType = eventType;
    }

    public EntryEventType getEventType() {
        return eventType;
    }
    
}
