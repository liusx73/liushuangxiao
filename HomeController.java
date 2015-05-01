package com.trading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
/**
 * 首页Controller
 * @author admin
 *
 */
public class HomeController {
	@RequestMapping(value = "/home")
    public String signinView() {
		
        return "/index";
    }
}
