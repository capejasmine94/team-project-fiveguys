package com.fiveguys.seller.controller;

import com.fiveguys.dto.*;
import com.fiveguys.seller.service.SellerCommunityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("api/seller")
public class SellerCommunityRestController {

    @Autowired
    SellerCommunityService sellerCommunityService;

    @RequestMapping("getSellerCommunityList")
    public Map<String, Object> getSellerCommunityList(
            HttpSession session,
            SellerCommunityPaginationDto sellerCommunityPaginationDto) {

        Map<String, Object> result = new HashMap<>();

        //페이지네이션 처리
        int totalPage = sellerCommunityService.selectSellerCommunityCount(sellerCommunityPaginationDto);

        int lastPageNumber = (int)Math.ceil((double)totalPage/sellerCommunityPaginationDto.getItemsPerPage());


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
        if(sellerDto!=null){

            result.put("login", true);
            result.put("sellerDto", sellerDto);
            result.put("totalPage", totalPage);

            if(sellerCommunityPaginationDto.getCurrentPage()==1){
                sellerCommunityPaginationDto.setItemsPerPage(3);
                result.put("selectSellerCommunityByPopularity",sellerCommunityService.selectSellerCommunityByPopularity(sellerCommunityPaginationDto,sellerDto.getSellerNumber()));
            }else{
                result.put("selectSellerCommunityByPopularity",null);
            }

            result.put("sellerCommunity", sellerCommunityService.selectSellerCommunityList(sellerCommunityPaginationDto, sellerDto.getSellerNumber()));
        }else{
            result.put("login", false);
        }
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

    @RequestMapping("sellerCommunityDetailPage")
    public  Map<String, Object> sellerCommunityDetailPage(HttpSession session, SellerCommunityLikeDto sellerCommunityLikeDto){

        Map<String, Object> response = new HashMap<>();
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");

        sellerCommunityService.updateSellerCommunityVisitCount(sellerCommunityLikeDto.getSellerCommunityNumber());

        sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());

        response.put("sellerDto", sellerDto);
        response.put("selectPreviousSellerCommunity",sellerCommunityService.selectPreviousSellerCommunity(sellerCommunityLikeDto.getSellerCommunityNumber()));
        response.put("selectNextSellerCommunity",sellerCommunityService.selectNextSellerCommunity(sellerCommunityLikeDto.getSellerCommunityNumber()));
        response.put("checkIfSellerCommunityLikeExists",sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto));
        response.put("sellerCommunityDetail", sellerCommunityService.selectSellerCommunityByIdWithSession(sellerCommunityLikeDto.getSellerCommunityNumber(),sellerDto.getSellerNumber()));

        return response;
    }



    @RequestMapping( "/sellerCommunityWriteProcess")
    public Map<String, Object> sellerCommunityWriteProcess(
            HttpSession session,
            @RequestParam("sellerCommunityTitle") String sellerCommunityTitle,
            @RequestParam("sellerCommunityContent") String sellerCommunityContent,
            @RequestPart("oneSellerCommunityImage") MultipartFile oneSellerCommunityImage,
            @RequestPart("multipleSellerCommunityImageList") List<MultipartFile> multipleSellerCommunityImageList
    ) {

        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        Map<String, Object> response = new HashMap<>();

        if(sellerDto!=null){
            List<SellerCommunityImageDetailDto> sellerCommunityImageDetailDtoList = new ArrayList<>();
            SellerCommunityDto sellerCommunityDto = new SellerCommunityDto();
            sellerCommunityDto.setSellerCommunityTitle(sellerCommunityTitle);
            sellerCommunityDto.setSellerCommunityContent(sellerCommunityContent);
            sellerCommunityDto.setSellerNumber(sellerDto.getSellerNumber());

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
    public Map<String, Object> sellerCommunityCommentInsertProcess(HttpSession session,@RequestBody SellerCommunityCommentDto sellerCommunityCommentDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        Map<String, Object> response = new HashMap<>();
        if(sellerDto!=null){
            sellerCommunityService.insertSellerCommunityComment(sellerCommunityCommentDto);
            response.put("success", true);
        }else{
            response.put("success", false);
        }

        return response;
    }


    @RequestMapping("sellerCommunityReplyInsertProcess")
    public Map<String, Object> sellerCommunityReplyInsertProcess(HttpSession session,@RequestBody SellerCommunityReplyDto sellerCommunityReplyDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        Map<String, Object> response = new HashMap<>();
        if(sellerDto!=null){
            sellerCommunityService.insertSellerCommunityReply(sellerCommunityReplyDto);
            response.put("success", true);
        }else{
            response.put("success", false);
        }
        return response;
    }


    @RequestMapping("sellerCommunityLikeProcess")
    public Map<String, Object> sellerCommunityLikeProcess(HttpSession session, @RequestBody SellerCommunityLikeDto sellerCommunityLikeDto){
        Map<String, Object> response = new HashMap<>();


        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto!=null){
            int checkIfSellerCommunityLikeExists = sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);
            if(checkIfSellerCommunityLikeExists==0){
                sellerCommunityService.insertSellerCommunityLike(sellerCommunityLikeDto);
            }else{
                sellerCommunityService.deleteSellerCommunityLike(sellerCommunityLikeDto);
            }
            response.put("success", true);
        }else{
            response.put("success", false);
        }

        return response;
    }

    @RequestMapping("sellerCommentLikeProcess")
    public Map<String, Object>   sellerCommentLikeProcess(HttpSession session,@RequestBody SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");


        Map<String, Object> response = new HashMap<>();

        if(sellerDto!=null){
            if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().isEmpty()){
                sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("like");
            }else if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().equals("dislike")){
                sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("like");
            }else{
                sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("");
            }
            sellerCommunityService.updateSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
            response.put("success", true);
        }else{
            response.put("success", false);
        }

        return response;
    }

    @RequestMapping("sellerCommentDisLikeProcess")
    public Map<String, Object>  sellerCommentDisLikeProcess(HttpSession session,@RequestBody  SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");


        Map<String, Object> response = new HashMap<>();

        if(sellerDto!=null){
            if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().isEmpty()){
                sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("dislike");
            }else if(sellerCommunityCommentLikeStatusDto.getSellerCommentLikeStatus().equals("like")){
                sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("dislike");
            }else{
                sellerCommunityCommentLikeStatusDto.setSellerCommentLikeStatus("");
            }
            sellerCommunityService.updateSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
            response.put("success", true);
        }else{
            response.put("success", false);
        }

        return response;
    }

    @RequestMapping("sellerReplyLikeProcess")
    public  Map<String, Object>  sellerReplyLikeProcess(HttpSession session,@RequestBody SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");

        Map<String, Object> response = new HashMap<>();
        if(sellerDto!=null){
            if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().isEmpty()){
                sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("like");
            }else if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().equals("dislike")){
                sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("like");
            }else{
                sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("");
            }
            sellerCommunityService.updateSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto);
            response.put("success", true);
        }else{
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping("sellerReplyDisLikeProcess")
    public Map<String, Object> sellerReplyDisLikeProcess(HttpSession session,@RequestBody SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto){
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");

        Map<String, Object> response = new HashMap<>();
        if(sellerDto!=null){
            if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().isEmpty()){
                sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("dislike");
            }else if(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyLikeStatus().equals("like")){
                sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("dislike");
            }else{
                sellerCommunityReplyLikeStatusDto.setSellerCommunityReplyLikeStatus("");
            }
            sellerCommunityService.updateSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto);
            response.put("success", true);
        }else{
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping("getChartRegisterCountPerMonth")
    public Map<String, Object> getChartRegisterCountPerMonth(HttpSession session){
        Map<String, Object> response = new HashMap<>();
        response.put("getChartRegisterCountPerMonth", sellerCommunityService.getChartRegisterCountPerMonth());
        return response;
    }

    @RequestMapping("getChartRegisterCount")
    public Map<String, Object> getChartRegisterCount(HttpSession session){
        Map<String, Object> response = new HashMap<>();
        response.put("getChartRegisterCount",sellerCommunityService.getChartRegisterCount());
        return response;
    }
    @RequestMapping("getPieRegisterCount")
    public Map<String, Object> getPieRegisterCount(HttpSession session){
        Map<String, Object> response = new HashMap<>();
        response.put("getPieRegisterCount",sellerCommunityService.getPieRegisterCount());
        return response;
    }
}
