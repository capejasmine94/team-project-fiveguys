package com.fiveguys.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("customer")
public class CustomerController {

    @RequestMapping("mainPage")
    public String mainPage() {
        return "customer/mainPage";
    }

    @RequestMapping("myPage")
    public String myPage() {

        return "customer/myPage";
    }
}
