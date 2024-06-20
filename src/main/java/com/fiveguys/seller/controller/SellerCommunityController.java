package com.fiveguys.seller.controller;

import com.fiveguys.seller.service.SellerCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seller")
public class SellerCommunityController {

    @Autowired
    SellerCommunityService sellerCommunityService;

    @RequestMapping("sellerCommunity")
    public String sellerCommunity(){
        return "seller/sellerCommunity";
    }
}
