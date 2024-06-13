package com.fiveguys.seller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seller")
public class SellerController {

    @RequestMapping("mainPage")
    public String mainPage() {
        return "seller/mainPage";
    }

    @RequestMapping("sellerCommunity")
    public String sellerCommunity(){
        return "seller/sellerCommunity";
    }

}
