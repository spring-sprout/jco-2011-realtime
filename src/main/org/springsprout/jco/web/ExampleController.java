package org.springsprout.jco.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springsprout.jco.example.HelloWorldStreamer;

@Controller
public class ExampleController {
	
	@RequestMapping(value="/examples-helloworld")
	public ModelAndView example() {
		try {
			new HelloWorldStreamer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("examples/helloworld", "message", "Hello World");
	}
	
}
