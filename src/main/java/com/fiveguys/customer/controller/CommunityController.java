package com.fiveguys.customer.controller;

import com.fiveguys.customer.service.CommunityService;
import com.fiveguys.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public String communityWriteProcess(HttpSession session, CommunityDto params,@RequestParam("uploadFiles") MultipartFile[] uploadFiles,@RequestParam("uploadFileMain") MultipartFile uploadFileMain){

        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");
        int userPk = customerDto.getCustomerNumber();

        List<CommunityDetailImageDto> communityDetailImageDtoList = new ArrayList<>();

        // 대표 이미지 처리
        if(uploadFiles != null){
            for(MultipartFile multipartFile : uploadFiles){
                if(multipartFile.isEmpty()){
                    continue;
                }
                String imagePath = saveFile(multipartFile);

                //DB 작업용 Dto 생성
                CommunityDetailImageDto communityDetailImageDto = new CommunityDetailImageDto();
                communityDetailImageDto.setMultipleImage(imagePath);

                communityDetailImageDtoList.add(communityDetailImageDto);
            }
        }

        //멀티 이미지
        if (!uploadFileMain.isEmpty()) {
            String mainImagePath = saveFile(uploadFileMain);
            params.setCommunityImage(mainImagePath);
        }

        params.setCustomerNumber(userPk);

        communityService.insertCommunityWrite(params,communityDetailImageDtoList);

        return "redirect:./communityPage";
    }

    private String saveFile(MultipartFile file) {
        // 경로 설정, 폴더 위치
        String rootPath = "C:/fiveguys_image/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String directory = sdf.format(new Date());
        File createDirectory = new File(rootPath + directory);

        if (!createDirectory.exists()) {
            createDirectory.mkdirs();
        }

        // 파일명 조작

        String originalFileName = file.getOriginalFilename();
        String userId = UUID.randomUUID().toString();

        long addMilliTime = System.currentTimeMillis();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String filename = userId + "_" + addMilliTime + fileExtension;

        // 파일 저장
        try {
            file.transferTo(new File(rootPath + directory + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return directory + filename;
    }

    //자유게시판 상세글보기 페이지
    @RequestMapping("communityReadPage")
    public String communityReadPage(HttpSession session, Model model, @RequestParam("communityNumber") int communityNumber){
        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");

        communityService.updateVisitCount(communityNumber);
        //게시글 정보
        Map<String, Object> communityData = communityService.selectCommunityNumber(communityNumber);
        model.addAttribute("communityData", communityData);

        List<Map<String, Object>> communityCommentDtoList = communityService.selectCommunityCommentList(communityNumber,customerDto.getCustomerNumber());
//        System.out.println(communityCommentDtoList);
        model.addAttribute("communityCommentDtoList", communityCommentDtoList);

        // 게시글 좋아요
        CommunityLikeDto communityLikeDto = new CommunityLikeDto();

        communityLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
        communityLikeDto.setCommunityNumber(communityNumber);

        CommunityLikeDto communityLikeDto1 = communityService.selectCommunityLike(communityLikeDto);
        model.addAttribute("communityLikeDto1", communityLikeDto1);

        int likeCount = communityService.selectCountCommunityLike(communityNumber);
        model.addAttribute("likeCount", likeCount);


        //댓글 리스트(좋아요상태, 카운트)
//        List<Map<String, Object>> selectCommentStatusLikeList = communityService.selectCommentLikeList(communityNumber);
//        model.addAttribute("selectCommentStatusLikeList", selectCommentStatusLikeList);

        //이미지
        List<CommunityDetailImageDto> communityImageDtoFile = communityService.selectCommunityDatailImageDtoList(communityNumber);
        model.addAttribute("communityImageDtoFile", communityImageDtoFile);

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
    public String communityUpdateProcess(CommunityDto params, @RequestParam("uploadFiles") MultipartFile[] uploadFiles){

        List<CommunityDetailImageDto> communityDetailImageDtoList = new ArrayList<>();

        // 멀티 이미지 처리
        if(uploadFiles != null){
            for(MultipartFile multipartFile : uploadFiles){
                if(multipartFile.isEmpty()){
                    continue;
                }
                String imagePath = saveFile(multipartFile);

                //DB 작업용 Dto 생성
                CommunityDetailImageDto communityDetailImageDto = new CommunityDetailImageDto();
                communityDetailImageDto.setMultipleImage(imagePath);

                communityDetailImageDtoList.add(communityDetailImageDto);
            }
        }

        communityService.updateCommunityPage(params);
        communityService.deleteCommunityImage(params.getCommunityNumber());
        communityService.insertImageDto(params.getCommunityNumber(), communityDetailImageDtoList);

        return "redirect:./communityPage";
    }

    //댓글쓰기
    @RequestMapping("communityCommentProcess")
    public String communityCommentProcess(HttpSession session, CommunityCommentDto communityCommentDto){

        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");
        communityCommentDto.setCustomerNumber(customerDto.getCustomerNumber());

        communityService.insertCommunityComment(communityCommentDto);

        return "redirect:./communityReadPage?communityNumber=" + communityCommentDto.getCommunityNumber();
    }

    //게시판 좋아요
    @RequestMapping("communityLikeProcess")
    public String communityLikeProcess(CommunityLikeDto communityLikeDto){
        communityService.insertCommunityLike(communityLikeDto);

        return "redirect:./communityReadPage?communityNumber=" + communityLikeDto.getCommunityNumber();
    }

    @RequestMapping("deleteLikeNumber")
    public String deleteLikeNumber(CommunityLikeDto communityLikeDto){

        CommunityLikeDto communityLikeDto1 = communityService.selectCommunityLike(communityLikeDto);
        communityService.deleteLikeNumber(communityLikeDto1.getLikeNumber());

        return "redirect:./communityReadPage?communityNumber=" + communityLikeDto.getCommunityNumber();
    }

    //게시판 댓글 좋아요
    @RequestMapping("communityCommentLikeProcess")
    public String communityCommentLikeProcess(CommentLikeStatusDto commentLikeStatusDto, @RequestParam("communityNumber") int communityNumber){

        communityService.selectCommentLike(commentLikeStatusDto);
        if(communityService.selectCommentLike(commentLikeStatusDto) == null){
            communityService.insertCommentLike(commentLikeStatusDto);
        }else{
            communityService.deleteCommentLikeNumber(commentLikeStatusDto.getCommentLikeNumber());
        }

        return "redirect:./communityReadPage?communityNumber=" + communityNumber;
    }

    @RequestMapping("deleteCommentLikeNumber")
    public String deleteCommentLikeNumber(CommentLikeStatusDto commentLikeStatusDto, @RequestParam("communityNumber") int communityNumber){
        CommentLikeStatusDto commentLikeStatusDto1 = communityService.selectCommentLike(commentLikeStatusDto);
        communityService.deleteCommentLikeNumber(commentLikeStatusDto1.getCommentLikeNumber());

        return "redirect:./communityReadPage?communityNumber=" + communityNumber;

    }

    //대댓글 페이지 연결
    @RequestMapping("communityCommentReply")
    public String communityCommentReply(@RequestParam("commentNumber") int commentNumber, @RequestParam("communityNumber")int communityNumber, Model model){
        model.addAttribute("commentNumber", commentNumber);
        model.addAttribute("communityNumber", communityNumber);
        return "customer/communityCommentReply";
    }


    //대댓글 쓰기
    @RequestMapping("communityCommentReplyProcess")
    public String communityCommentReplyProcess(HttpSession session, @RequestParam("commentNumber") int commentNumber, @RequestParam("communityCommentReplyText") String communityCommentReplyText, @RequestParam("communityNumber") int communityNumber){

        System.out.println("commentNumber"+commentNumber);
        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");

        CommunityCommentReplyDto communityCommentReplyDto  = new CommunityCommentReplyDto();
        communityCommentReplyDto.setCustomerNumber(customerDto.getCustomerNumber());
        communityCommentReplyDto.setCommentNumber(commentNumber);
        communityCommentReplyDto.setCommunityCommentReplyText(communityCommentReplyText);

        communityService.insertCommunityCommentReply(communityCommentReplyDto);

        return "redirect:/customer/communityReadPage?communityNumber=" + communityNumber;
    }

    @RequestMapping("communityActivityHistory")
    public String communityActivityHistory(){


        return "/customer/communityActivityHistory";
    }



}
