// New UpiPaymentController.java (package com.supermarket.controller;)
package com.supermarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UpiPaymentController {
    @GetMapping("/upi-payment")
    public String upiPaymentPage() {
        return "upi-payment";
    }
}