package org.springsprout.realtime.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springsprout.realtime.oxquiz.EntryStore;

@Controller
public class QxQuizController {
    
    @Autowired
    private EntryStore entryStore;
    
    @RequestMapping("/")
    public String oxquiz(Model model) {
        model.addAttribute("entrys", entryStore.getAllEntry());
        return "/oxquiz";
    }

}
