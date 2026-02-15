package com.supermarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login-page")
    public String loginPage() {
        return "login"; // loads login.html
    }
}
