package com.fiveguys.customer.controller;

import com.fiveguys.customer.service.CommunityService;
import com.fiveguys.dto.CommunityDto;
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






}
