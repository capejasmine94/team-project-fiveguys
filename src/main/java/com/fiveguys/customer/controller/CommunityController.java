package com.fiveguys.customer.controller;

import com.fiveguys.customer.service.CommunityService;
import com.fiveguys.dto.CommunityDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CommunityController {

    private CommunityService communityService;

    @RequestMapping("communityPage")
    public String communityPage(Model model){
        List<Map<String, Object>> selectCommunityList = communityService.selectCommunityList();

        model.addAttribute("selectCommunityList", selectCommunityList);

        return "redirect:./communityPage";
    }

    @RequestMapping("communityWritePage")
    public String communityWritePage(){

        return "customer/communityWritePage";
    }

    @RequestMapping("communityWriteProcess")
    public String communityWriteProcess(HttpSession session, CommunityDto params){

        CommunityDto sessionUserInfo = (CommunityDto)session.getAttribute("sessionUserInfo");
        int userPk = sessionUserInfo.getCommunityNumber();

        params.setCustomerNumber(userPk);

        communityService.insertCommunity(params);

        return "redirect:./communityPage";
    }

    @RequestMapping("communityReadPage")
    public String communityReadPage(){

//        Map<String, Object> communityDataList = communityService.selectCommunityNumber();


        return "customer/communityReadPage";
    }





}
