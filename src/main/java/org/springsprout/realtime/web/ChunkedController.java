/**
 * ©2011 Looah, LLC
 * jco-2011-realtime
 * created by isyoon Jun 5, 2011 2:17:47 PM
 */
package org.springsprout.realtime.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springsprout.realtime.chunked.ChatStore;

@Controller
public class ChunkedController {
    
    @Autowired
    ChatStore chatStore;
    
    ObjectMapper om = new ObjectMapper();
    
    @RequestMapping("/chatListener")
    public void chatListener(HttpServletRequest req, HttpServletResponse res, @RequestParam()String uuid) throws Exception {
        ServletOutputStream out = res.getOutputStream();
        try {
            res.setContentType("application/json; charset=utf-8;");
            res.setHeader("Connection", "close");
            chatStore.enterStore(uuid);
            JsonGenerator generator = om.getJsonFactory().createJsonGenerator(res.getOutputStream(), JsonEncoding.UTF8);
            while(true){
                om.writeValue(generator,chatStore.getData(uuid));
                synchronized (this) {
                    wait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chatStore.remveStore(uuid);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    @RequestMapping("/sendMsg")
    public ModelAndView sendMsg(@RequestParam()String name, @RequestParam()String msg, Model model){
        model.addAttribute("isSuccess",chatStore.putMessage(name, msg));
        synchronized (this) {
           notifyAll();
        }
        return new ModelAndView("mappingJacksonJsonView");
    }
}
