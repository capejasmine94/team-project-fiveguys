package com.fiveguys.master.controller;

import com.fiveguys.dto.MasterReplyDto;
import com.fiveguys.master.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return "redirect:/master/mainPage";
    }

}
