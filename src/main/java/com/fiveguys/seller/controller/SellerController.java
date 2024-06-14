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

    @RequestMapping("materialMenuPage1")
    public String materialMenuPage1() {
        return "/seller/materialMenuPage1";
    }

    @RequestMapping("materialMenuPage2")
    public String materialMenuPage2() {
        return "/seller/materialMenuPage2";
    }

    @RequestMapping("materialMenuPage3")
    public String materialMenuPage3() {
        return "/seller/materialMenuPage3";
    }

    @RequestMapping("materialMenuPage5")
    public String materialMenuPage5() {
        return "/seller/materialMenuPage5";
    }

    @RequestMapping("materialMenuPage6")
    public String materialMenuPage6() {
        return "/seller/materialMenuPage6";
    }


    @RequestMapping("materialMenuPage4")
    public String materialMenuPage4() {
        return "/seller/materialMenuPage4";
    }


    @RequestMapping("sellerReviewPage")
    public String sellerReviewPage() {
        return "seller/sellerReviewPage";

    }

}
