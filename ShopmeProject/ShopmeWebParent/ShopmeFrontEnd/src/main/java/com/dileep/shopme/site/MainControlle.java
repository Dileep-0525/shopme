package com.dileep.shopme.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainControlle {

	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
}
