package com.supermarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillingController {

    @GetMapping("/billing")
    public String billing() {
        return "billing";
    }
}