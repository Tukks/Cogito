package com.tukks.cogito.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConfigurationController {
	@RequestMapping(value = "/{path:[^.]*}")
	public String redirect() {
		// Forward to home page so that route is preserved.(i.e forward:/intex.html)
		return "forward:/";
	}
}
