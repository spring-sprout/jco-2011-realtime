package org.springsprout.realtime.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springsprout.realtime.oxquiz.EntryStore;

@Controller
public class QxQuizController {
    
    @Autowired EntryStore entryStore;
    
    @RequestMapping("/")
    public String oxquiz(HttpSession session, Model model) {
        entryStore.forgetEntry((String) session.getAttribute("entryId"));
        model.addAttribute("entrys", entryStore.getAllEntry());
        
        return "/oxquiz";
    }
    
    @RequestMapping("/entryIn")
    public void entryInOut(@RequestParam String entryId, HttpSession session) {
        session.setAttribute("entryId", entryId);
    }
    
    @RequestMapping("/admin")
    public String oxquizAdmin(Model model) {
    	model.addAttribute("entrys", entryStore.getAllEntry());
    	
    	return "/oxquizadmin";
    }

}
