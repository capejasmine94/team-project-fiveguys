package com.fiveguys.seller.controller;

import com.fiveguys.dto.*;
import com.fiveguys.dto.SellerCommunityDto;
import com.fiveguys.dto.SellerCommunityImageDetailDto;
import com.fiveguys.dto.EventDetailImageDto;
import com.fiveguys.seller.service.SellerCommunityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("seller")
public class SellerCommunityController {

    @Autowired
    SellerCommunityService sellerCommunityService;

    @RequestMapping("sellerCommunity")
    public String sellerCommunity(Model model,SellerCommunityPaginationDto sellerCommunityPaginationDto){

        //페이지네이션 처리
        int totalPage = sellerCommunityService.selectSellerCommunityCount();
        System.out.println(totalPage);

        int lastPageNumber=0;

        if(totalPage % sellerCommunityPaginationDto.getItemsPerPage()==0){
            lastPageNumber = (int)Math.ceil((double)totalPage/sellerCommunityPaginationDto.getItemsPerPage())-1;
        }else{
            lastPageNumber = (int)Math.ceil((double)totalPage/sellerCommunityPaginationDto.getItemsPerPage());
        }

        int startPage =((sellerCommunityPaginationDto.getCurrentPage()-1)/5)*5+1;
        int endPage =((sellerCommunityPaginationDto.getCurrentPage()-1)/5+1)*5;

        if(endPage>lastPageNumber){
            endPage = lastPageNumber;
        }

        System.out.println(startPage);
        System.out.println(endPage);

        sellerCommunityPaginationDto.setStartPage(startPage);
        sellerCommunityPaginationDto.setEndPage(endPage);
        sellerCommunityPaginationDto.setPaginationPage(lastPageNumber);

        model.addAttribute("sellerCommunityPaginationDto", sellerCommunityPaginationDto);
        model.addAttribute("sellerCommunity", sellerCommunityService.selectSellerCommunityList(sellerCommunityPaginationDto));
        return "seller/sellerCommunity";
    }

    @RequestMapping("sellerCommunityWritePage")
    public String sellerCommunityWritePage(HttpSession session){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto==null){
            return "redirect:/login/sellerLogin";
        }
        return "seller/sellerCommunityWritePage";
    }

    @RequestMapping("sellerCommunityWriteProcess")
    public String sellerCommunityWriteProcess(SellerCommunityDto sellerCommunityDto, MultipartFile oneSellerCommunityImage, MultipartFile[] multipleSellerCommunityImageList){

        List<SellerCommunityImageDetailDto> sellerCommunityImageDetailDtoList = new ArrayList<>();

        // 대표 이미지 저장 및 경로 설정
        if (!oneSellerCommunityImage.isEmpty()) {
            String mainImagePath = saveFile(oneSellerCommunityImage);
            sellerCommunityDto.setSellerCommunityImage(mainImagePath);
        }

        // 상세 이미지 저장 및 경로 설정
        if (multipleSellerCommunityImageList != null) {
            for (MultipartFile file : multipleSellerCommunityImageList) {
                if (!file.isEmpty()) {
                    String imagePath = saveFile(file);

                    // 상품 상세 이미지 DB에 저장
                    SellerCommunityImageDetailDto sellerCommunityImageDetailDto = new SellerCommunityImageDetailDto();
                    sellerCommunityImageDetailDto.setSellerCommunityImageList(imagePath);
                    sellerCommunityImageDetailDtoList.add(sellerCommunityImageDetailDto);
                }
            }
        }
        sellerCommunityService.insertSellerCommunityWrite(sellerCommunityDto,sellerCommunityImageDetailDtoList);
        return "redirect:./sellerCommunity";
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

    @RequestMapping("sellerCommunityDetail")
    public String sellerCommunityDetail(
            Model model,
            int sellerCommunityNumber,
            HttpSession session
    ){

        //잠주 커뮤니티는 점주만 봐야 하므로 나중에 세션없는 부분 없애야 함
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto!=null){
            SellerCommunityLikeDto sellerCommunityLikeDto = new SellerCommunityLikeDto();
            sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());
            sellerCommunityLikeDto.setSellerCommunityNumber(sellerCommunityNumber);
            model.addAttribute("checkIfSellerCommunityLikeExists",sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto));
            model.addAttribute("sellerCommunityDetail", sellerCommunityService.selectSellerCommunityByIdWithSession(sellerCommunityNumber,sellerDto.getSellerNumber()));
        }else{
            model.addAttribute("sellerCommunityDetail", sellerCommunityService.selectSellerCommunityById(sellerCommunityNumber));
        }
        sellerCommunityService.updateSellerCommunityVisitCount(sellerCommunityNumber);
        return "seller/sellerCommunityDetail";
    }

    @RequestMapping("sellerCommunityCommentInsertProcess")
    public String sellerCommunityCommentInsertProcess(HttpSession session, SellerCommunityCommentDto sellerCommunityCommentDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto==null){
            return "redirect:/login/sellerLogin";
        }
        sellerCommunityService.insertSellerCommunityComment(sellerCommunityCommentDto);
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityCommentDto.getSellerCommunityNumber();
    }
    @RequestMapping("sellerCommunityReplyInsertProcess")
    public String sellerCommunityReplyInsertProcess(HttpSession session, SellerCommunityReplyDto sellerCommunityReplyDto,int sellerCommunityNumber){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto==null){
            return "redirect:/login/sellerLogin";
        }
        sellerCommunityService.insertSellerCommunityReply(sellerCommunityReplyDto);
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }

    @RequestMapping("sellerCommunityLikeProcess")
    public String sellerCommunityLikeProcess(HttpSession session, SellerCommunityLikeDto sellerCommunityLikeDto,int sellerCommunityNumber){
        int checkIfSellerCommunityLikeExists = sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);
        if(checkIfSellerCommunityLikeExists==0){
            sellerCommunityService.insertSellerCommunityLike(sellerCommunityLikeDto);
        }else{
            sellerCommunityService.deleteSellerCommunityLike(sellerCommunityLikeDto);
        }
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }

    @RequestMapping("sellerCommentLikeProcess")
    public String sellerCommentLikeProcess(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto,int sellerCommunityNumber){
        if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().isEmpty()){
            sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("like");
        }else if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().equals("dislike")){
            sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("like");
        }else{
            sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("");
        }
        sellerCommunityService.updateSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }

    @RequestMapping("sellerCommentDisLikeProcess")
    public String sellerCommentDisLikeProcess(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto,int sellerCommunityNumber){
        if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().isEmpty()){
            sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("dislike");
        }else if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().equals("like")){
            sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("dislike");
        }else{
            sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("");
        }
        sellerCommunityService.updateSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }

    @RequestMapping("sellerReplyLikeProcess")
    public String sellerReplyLikeProcess(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto,int sellerCommunityNumber){
        if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().isEmpty()){
            sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("like");
        }else if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().equals("dislike")){
            sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("like");
        }else{
            sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("");
        }
        sellerCommunityService.updateSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto);
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }
    @RequestMapping("sellerReplyDisLikeProcess")
    public String sellerReplyDisLikeProcess(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto,int sellerCommunityNumber){
        if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().isEmpty()){
            sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("dislike");
        }else if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().equals("like")){
            sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("dislike");
        }else{
            sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("");
        }
        sellerCommunityService.updateSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto);
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }

}
