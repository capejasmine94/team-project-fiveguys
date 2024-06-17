package com.fiveguys.customer.controller;

import com.fiveguys.customer.service.CommunityService;
import com.fiveguys.dto.CommunityCommentDto;
import com.fiveguys.dto.CommunityDto;
import com.fiveguys.dto.CustomerDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @RequestMapping("communityPage")
    public String communityPage(Model model){
        List<Map<String, Object>> selectCommunityList = communityService.selectCommunityList();

        model.addAttribute("selectCommunityList", selectCommunityList);

        return "customer/communityPage";
    }

    @RequestMapping("communityWritePage")
    public String communityWritePage(){

        return "customer/communityWritePage";
    }

    //자유게시판 글쓰기프로세스
    @RequestMapping("communityWriteProcess")
    public String communityWriteProcess(HttpSession session, CommunityDto params){
//        customerNumber를 가져오는 로직인데 일단 계속 에러떠서 주석처리.
//        현재 customerNumber는 모두 0인 상태로 글쓰기 DB는 들어감.
        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");
        int userPk = customerDto.getCustomerNumber();

        params.setCustomerNumber(userPk);

        communityService.insertCommunityWrite(params);

        return "redirect:./communityPage";
    }

    //자유게시판 상세글보기 페이지
    @RequestMapping("communityReadPage")
    public String communityReadPage(Model model,@RequestParam("communityNumber") int communityNumber){

        communityService.updateVisitCount(communityNumber);

        Map<String, Object> communityData = communityService.selectCommunityNumber(communityNumber);
        model.addAttribute("communityData", communityData);

        return "customer/communityReadPage";
    }

    //자유게시판 글 삭제
    @RequestMapping("deleteCommunityPageProcess")
    public String deleteCommunityPageProcess(@RequestParam("communityNumber") int communityNumber){
        communityService.deleteCommunityPage(communityNumber);
        return "redirect:./communityPage";
    }

    //자유게시판 글 수정
    @RequestMapping("updateCommunityPage")
    public String updateCommunityPage(Model model, @RequestParam("communityNumber") int communityNumber){

        Map<String, Object> communityData = communityService.selectCommunityNumber(communityNumber);
        model.addAttribute("communityData", communityData);

        return "customer/communityUpdatePage";
    }

    //자유게시판 글 수정프로세스
    @RequestMapping("communityUpdateProcess")
    public String communityUpdateProcess(CommunityDto params){

        communityService.updateCommunityPage(params);

        return "redirect:./communityPage";
    }

    //댓글쓰기
    @RequestMapping("communityCommentProcess")
    public String communityCommentProcess(CommunityCommentDto communityCommentDto){
        communityService.insertCommunityComment(communityCommentDto);

        return "redirect:./communityReadPage?communityNumber" + communityCommentDto.getCommunityNumber();
    }






}
