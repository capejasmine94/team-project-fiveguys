package com.fiveguys.seller.controller;

import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.dto.SellerReviewDto;
import com.fiveguys.seller.service.SellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @RequestMapping("deleteSellerRecentOrder")
    public String deleteSellerRecentOrder() {
        sellerService.deleteSellerRecentOrder();

        return "redirect:/seller/orderPage";
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
    public String sellerReviewPage(Model model) {

        List<Map<String, Object>> reviewInform = sellerService.selectAllSellerReview();
        model.addAttribute("reviewInform", reviewInform);

        return "/seller/sellerReviewPage";
    }


    @RequestMapping("myInformPage")
    public String myInformPage() {
        return "/seller/myInformPage";
    }


    @RequestMapping("orderCheckPage")
    public String orderCheckPage(HttpSession session, Model model) {
        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();
        List<Map<String, Object>> sellerOrderList = sellerService.selectAllSellerOrder(sellerNumber);
        model.addAttribute("sellerOrderList", sellerOrderList);

        return "/seller/orderCheckPage";
    }


    @RequestMapping("orderDetailCheckPage")
    public String orderDetailCheckPage(Model model, SellerOrderDto sellerOrderDto, int id) {

        List<Map<String, Object>> sellerOrderList = sellerService.selectSameSellerOrder(sellerOrderDto, id);
        model.addAttribute("sellerOrderList", sellerOrderList);

        return "/seller/orderDetailCheckPage";
    }


    @RequestMapping("reviewChoicePage")
    public String reviewChoicePage(HttpSession session, Model model) {



        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();
        List<Map<String, Object>> sellerOrderList = sellerService.selectAllSellerOrder(sellerNumber);
        model.addAttribute("sellerOrderList", sellerOrderList);

        return "/seller/reviewChoicePage";
    }


    @RequestMapping("reviewWritePage")
    public String reviewWritePage(Model model, SellerOrderDto sellerOrderDto, int id) {

        SellerOrderDto sellerOrderInform = sellerService.selectSellerOrderInform(id);
        List<Map<String, Object>> sellerOrderList = sellerService.selectSameSellerOrder(sellerOrderDto, id);
        model.addAttribute("sellerOrderList", sellerOrderList);
        model.addAttribute("sellerOrderInform", sellerOrderInform);

        return "/seller/reviewWritePage";
    }


    @RequestMapping("insertSellerReview")
    public String insertSellerReview(SellerReviewDto sellerReviewDto) {

        sellerService.insertSellerReview(sellerReviewDto);

        return "/seller/mainPage";
    }


    @RequestMapping("reviewDetailPage")
    public String reviewDetailPage(Model model, int id) {


        Map<String, Object> reviewInform = sellerService.selectSellerReview(id);
        model.addAttribute("reviewInform", reviewInform);

        return "/seller/reviewDetailPage";
    }





}
