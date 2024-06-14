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


    @RequestMapping("orderPage")
    public String orderPage() {

        return "seller/orderPage";
    }

    @RequestMapping("materialMenuPage")
    public String materialMenuPage() {
        return "seller/materialMenuPage";
    }


    @RequestMapping("sellerReviewPage")
    public String sellerReviewPage() {
        return "seller/sellerReviewPage";

    }








}
