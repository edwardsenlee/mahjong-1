package com.ahliu.test.mahjong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView test() {
		final ModelAndView model = new ModelAndView("TestPage");
		model.addObject("msg", "hello world!");
		return model;
	}
}
