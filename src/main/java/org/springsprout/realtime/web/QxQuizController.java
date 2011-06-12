package org.springsprout.realtime.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springsprout.realtime.oxquiz.QuizStore;
import org.springsprout.realtime.oxquiz.entry.Entry;
import org.springsprout.realtime.oxquiz.entry.EntryEvent;
import org.springsprout.realtime.oxquiz.entry.EntryEventType;
import org.springsprout.realtime.oxquiz.entry.EntryManager;

@Controller
public class QxQuizController {
    
    private Logger logger = LoggerFactory.getLogger(QxQuizController.class);
    
    @Autowired ApplicationContext applicationContext;
    @Autowired EntryManager entryManager;
    
    @RequestMapping("/")
    public String oxquiz(HttpSession session, Model model) {
        final String clientId = StringUtils.trimToEmpty((String) session.getAttribute("entryId"));
        
        // 기존의 사용자가 다시 페이지를 요청했을때 streamhub 에게 알려준다.
        Entry entry = entryManager.getEntry(clientId);
        if(entry != null)
            applicationContext.publishEvent(new EntryEvent(entry, EntryEventType.REFRESH));
        
        // 기존에 유지되던 아이디는 제거하고 내려보냄
        List<Entry> entrys = entryManager.getAllActiveEntrys();
        CollectionUtils.filter(entrys, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return !((Entry) object).getId().equals(clientId);
            }
        });
        model.addAttribute("entrys", entrys);
        
        return "/oxquiz";
    }
    
    @RequestMapping("/entryConn")
    public void entryConn(@RequestParam String entryId, HttpSession session) {
        Entry entry = entryManager.getEntry(entryId);
        if(entry != null) {
            entry.setSessionId(session.getId());
            applicationContext.publishEvent(new EntryEvent(entry, EntryEventType.ACTIVE));
        }
        
        session.setAttribute("entryId", entryId);
        
        logger.info("사용자({})를 기억해둡니다.", entryId);
    }
    
    @RequestMapping("/quizInit")
    public void quizInit() {
        logger.info("퀴즈 문제를 초기화합니다.");
        
        QuizStore.init();
    }
    
    @RequestMapping("/admin")
    public String oxquizAdmin(Model model) {
    	model.addAttribute("entrys", entryManager.getAllActiveEntrys());
    	
    	return "/oxquizadmin";
    }

}
