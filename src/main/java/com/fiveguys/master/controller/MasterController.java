package com.fiveguys.master.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("master")
public class MasterController {

    @RequestMapping("mainPage")
    public String mainPage() {
        return "master/mainPage";
    }
}
