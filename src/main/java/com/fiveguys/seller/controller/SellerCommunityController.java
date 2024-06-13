package com.fiveguys.seller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seller")
public class SellerCommunityController {

    @RequestMapping("sellerCommunity")
    public String sellerCommunity(){
        return "seller/sellerCommunity";
    }
}
