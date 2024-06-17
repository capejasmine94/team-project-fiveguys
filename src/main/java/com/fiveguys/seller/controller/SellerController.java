package com.fiveguys.seller.controller;

import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.seller.service.SellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("seller")
public class SellerController {

    @Autowired
    SellerService sellerService;


    @RequestMapping("mainPage")
    public String mainPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();

        List<Map<String, Object>> sellerOrderList = sellerService.selectRecentSellerOrder(sellerNumber);

        model.addAttribute("sellerOrderList", sellerOrderList);

        return "seller/mainPage";

    }

    @RequestMapping("orderPage")
    public String orderPage() {

        return "seller/orderPage";
    }

    @RequestMapping("sellerOrderProcess")
    public String sellerOrderProcess(HttpSession session, SellerOrderDto sellerOrderDto, @RequestParam("materialNumber") List<Integer> materialNumber) {
        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        sellerOrderDto.setSellerNumber(sellerDto.getSellerNumber());
        sellerService.insertSellerOrder(sellerOrderDto, materialNumber);


        return "redirect:/seller/orderDetailPage";
    }


    @RequestMapping("orderDetailPage")
    public String orderDetailPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();

        List<Map<String, Object>> sellerOrderList = sellerService.selectSellerOrder(sellerNumber);

        model.addAttribute("sellerOrderList", sellerOrderList);


        return "seller/orderDetailPage";
    }


    @RequestMapping("updateMaterialQuantity")
    public String updateMaterialQuantity(int[] sellerOrderQuantity, int[] sellerOrderNumber) {

        sellerService.updateMaterialQuantity(sellerOrderQuantity, sellerOrderNumber);

        return "redirect:/seller/orderSuccessPage";
    }



    @RequestMapping("orderSuccessPage")
    public String orderSuccessPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();

        List<Map<String, Object>> sellerOrderList = sellerService.selectSellerOrder(sellerNumber);

        model.addAttribute("sellerOrderList", sellerOrderList);

        return "seller/orderSuccessPage";
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
        return "/seller/sellerReviewPage";
    }

}
