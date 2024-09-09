package com.daurenassanbaev.firstpetproject.http.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String login() {
        log.info("Received login page");
        return "user/login";
    }

}
