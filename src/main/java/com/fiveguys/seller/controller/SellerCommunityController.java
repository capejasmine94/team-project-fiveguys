package com.fiveguys.seller.controller;

import com.fiveguys.dto.SellerCommunityDto;
import com.fiveguys.dto.SellerCommunityImageDetailDto;
import com.fiveguys.dto.eventDetailImageDto;
import com.fiveguys.seller.service.SellerCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String sellerCommunity(Model model){
        model.addAttribute("sellerCommunity", sellerCommunityService.selectSellerCommunityList());
        return "seller/sellerCommunity";
    }

    @RequestMapping("sellerCommunityWritePage")
    public String sellerCommunityWritePage(){
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
}
