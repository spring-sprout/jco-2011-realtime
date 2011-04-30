package org.springsprout.jco.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springsprout.jco.example.Chat;
import org.springsprout.jco.example.HelloWorldStreamer;

@Controller
public class ExampleController {
	
	@RequestMapping(value="/examples-helloworld")
	public ModelAndView helloworld() {
		try {
			new HelloWorldStreamer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("examples/helloworld", "message", "Hello World");
	}
	
	@RequestMapping(value="/examples-cometchat")
	public ModelAndView cometchat() {
		try {
			new Chat();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("examples/cometchat", "message", "Hello World");
	}
	
}
