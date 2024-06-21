package com.fiveguys.master.controller;

import com.fiveguys.dto.MasterReplyDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.master.service.MasterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @RequestMapping("mainPage")
    public String mainPage() {
        return "/master/mainPage";
    }


    @RequestMapping("logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login/masterLogin";
    }


    @RequestMapping("reviewPage")
    public String reviewPage(Model model) {

        List<Map<String, Object>> sellerReviewList = masterService.selectAllSellerReview();
        model.addAttribute("sellerReviewList", sellerReviewList);

        return "/master/reviewPage";
    }


    @RequestMapping("reviewDetailPage")
    public String reviewDetailPage(Model model, int id) {

        Map<String, Object> reviewInform =  masterService.selectSellerReview(id);
        model.addAttribute("reviewInform", reviewInform);

        return "/master/reviewDetailPage";
    }


    @RequestMapping("insertMasterReply")
    public String insertMasterReply(MasterReplyDto masterReplyDto) {
        masterService.insertMasterReply(masterReplyDto);

        return "/master/mainPage";
    }


    @RequestMapping("orderPage")
    public String orderPage(Model model, int id) {
        List<Map<String, Object>> sellerOrderInform = masterService.selectAllSellerOrder(id);

        model.addAttribute("sellerOrderInform", sellerOrderInform);

        return "/master/orderPage";
    }


    @RequestMapping("orderDetailPage")
    public String orderDetailPage(Model model, SellerOrderDto sellerOrderDto, int id) {


        SellerOrderDto sellerOrderDtos = masterService.selectSellerOrder(id);

        List<Map<String, Object>> sellerOrderInform = masterService.selectSameSellerOrder(sellerOrderDto, id);

        model.addAttribute("sellerOrderInform", sellerOrderInform);
        model.addAttribute("sellerOrderDto", sellerOrderDtos);

        return "/master/orderDetailPage";
    }



    @RequestMapping("updateOrderStatusProcessingShipment")
    public String updateOrderStatusProcessingShipment(@RequestParam("id") int id) {

        masterService.updateOrderStatusProcessingShipment(id);

        return "redirect:/master/orderDetailPage?id=" + id;
    }

    @RequestMapping("updateOrderStatusDelivery")
    public String updateOrderStatusDelivery(@RequestParam("id") int id) {

        masterService.updateOrderStatusDelivery(id);

        return "redirect:/master/orderDetailPage?id=" + id;
    }


    @RequestMapping("updateOrderStatusDeliveryCompleted")
    public String updateOrderStatusDeliveryCompleted(@RequestParam("id") int id) {

        masterService.updateOrderStatusDeliveryCompleted(id);

        return "redirect:/master/orderDetailPage?id=" + id;
    }


    @RequestMapping("materialRegisterPage")
    public String materialRegisterPage() {

        return "/master/materialRegisterPage";
    }






    @RequestMapping("materialCategoryRegisterPage")
    public String materialCategoryRegisterPage() {

        return "/master/materialCategoryRegisterPage";
    }

    @RequestMapping("insertMaterialCategory")
    public String insertMaterialCategory(String materialCategoryName) {

        masterService.insertMaterialCategory(materialCategoryName);

        return "redirect:/master/materialCategoryRegisterPage";
    }




}
