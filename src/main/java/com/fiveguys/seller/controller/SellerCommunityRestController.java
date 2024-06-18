package com.fiveguys.seller.controller;

import com.fiveguys.dto.*;
import com.fiveguys.seller.service.SellerCommunityService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("api/seller")
public class SellerCommunityRestController {

    @Autowired
    SellerCommunityService sellerCommunityService;

    @RequestMapping("getSellerCommunityList")
    public Map<String, Object> getSellerCommunityList(HttpSession session, SellerCommunityPaginationDto sellerCommunityPaginationDto) {
        Map<String, Object> result = new HashMap<String, Object>();

        //페이지네이션 처리
        int totalPage = sellerCommunityService.selectSellerCommunityCount();

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


        sellerCommunityPaginationDto.setStartPage(startPage);
        sellerCommunityPaginationDto.setEndPage(endPage);
        sellerCommunityPaginationDto.setPaginationPage(lastPageNumber);

        result.put("sellerCommunityPaginationDto", sellerCommunityPaginationDto);

        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        result.put("sellerCommunity", sellerCommunityService.selectSellerCommunityList(sellerCommunityPaginationDto,sellerDto.getSellerNumber()));

        return result;
    }

    @RequestMapping("sellerCommunityLike")
    public Map<String, Object> sellerCommunityLike(HttpSession session, SellerCommunityLikeDto sellerCommunityLikeDto) {
        Map<String, Object> response = new HashMap<>();
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto==null){
            response.put("success", false);
        }else{
            sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());
            int checkIfSellerCommunityLikeExists = sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);
            if(checkIfSellerCommunityLikeExists==0){
                sellerCommunityService.insertSellerCommunityLike(sellerCommunityLikeDto);
            }else {
                sellerCommunityService.deleteSellerCommunityLike(sellerCommunityLikeDto);
            }
            response.put("success", true);
        }
        return response;
    }


    public  Map<String, Object> sellerCommunityDetailPage(HttpSession session, SellerCommunityLikeDto sellerCommunityLikeDto){

        Map<String, Object> response = new HashMap<>();
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");

        sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());

        response.put("sellerDto", sellerDto);
        response.put("checkIfSellerCommunityLikeExists",sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto));
        response.put("sellerCommunityDetail", sellerCommunityService.selectSellerCommunityByIdWithSession(sellerCommunityLikeDto.getSellerCommunityNumber(),sellerDto.getSellerNumber()));

        return response;
    }

    //수정 필요함

    @PostMapping(value = "/api/seller/sellerCommunityWriteProcess", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> sellerCommunityWriteProcess(
            HttpSession session,
            @RequestParam("sellerCommunityTitle") String sellerCommunityTitle,
            @RequestParam("sellerCommunityContent") String sellerCommunityContent,
            @RequestPart("oneSellerCommunityImage") MultipartFile oneSellerCommunityImage,
            @RequestPart("multipleSellerCommunityImageList") List<MultipartFile> multipleSellerCommunityImageList
    ) {
        System.out.println("컨트롤러 젅달됨");

        System.out.println(sellerCommunityTitle);
        System.out.println(sellerCommunityContent);
        System.out.println(oneSellerCommunityImage);
        System.out.println(multipleSellerCommunityImageList);
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        Map<String, Object> response = new HashMap<>();

        if(sellerDto!=null){
            List<SellerCommunityImageDetailDto> sellerCommunityImageDetailDtoList = new ArrayList<>();
            SellerCommunityDto sellerCommunityDto = new SellerCommunityDto();
            sellerCommunityDto.setSellerCommunityTitle(sellerCommunityTitle);
            sellerCommunityDto.setSellerCommunityContent(sellerCommunityContent);

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
            response.put("inputSuccess",true);
        }else{
            response.put("inputSuccess", false);
        }
        return response;
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
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());

        if(checkIfSellerCommunityLikeExists==0){
            sellerCommunityService.insertSellerCommunityLike(sellerCommunityLikeDto);
        }else{
            sellerCommunityService.deleteSellerCommunityLike(sellerCommunityLikeDto);
        }
        return "redirect:./sellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    }

    @RequestMapping("sellerCommentLikeProcess")
    public String sellerCommentLikeProcess(HttpSession session, SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto,int sellerCommunityNumber){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        sellerCommunityCommentLikeStatusDto.setSellerNumber(sellerDto.getSellerNumber());

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
    public String sellerCommentDisLikeProcess(HttpSession session, SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto,int sellerCommunityNumber){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        sellerCommunityCommentLikeStatusDto.setSellerNumber(sellerDto.getSellerNumber());

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
    public String sellerReplyLikeProcess(HttpSession session, SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto,int sellerCommunityNumber){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        sellerCommunityReplyLikeStatusDto.setSellerNumber(sellerDto.getSellerNumber());

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
    public String sellerReplyDisLikeProcess(HttpSession session, SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto,int sellerCommunityNumber){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        sellerCommunityReplyLikeStatusDto.setSellerNumber(sellerDto.getSellerNumber());

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
