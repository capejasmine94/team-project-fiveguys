package com.fiveguys.seller.controller;

import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.dto.SellerReviewDto;
import com.fiveguys.dto.SellerReviewImageDto;
import com.fiveguys.seller.service.SellerService;
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
@RequestMapping("seller")
public class SellerController {

    @Autowired
    SellerService sellerService;



    @RequestMapping("mainPage")
    public String mainPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();

        List<Map<String, Object>> sellerOrderList = sellerService.selectRecentSellerOrder(sellerNumber);

        model.addAttribute("sellerOrderList", sellerOrderList);

        return "seller/mainPage";
    }



    @RequestMapping("orderPage")
    public String orderPage() {

        return "seller/orderPage";
    }

    @RequestMapping("sellerOrderProcess")
    public String sellerOrderProcess(HttpSession session, SellerOrderDto sellerOrderDto, @RequestParam("materialNumber") List<Integer> materialNumber) {
        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        sellerOrderDto.setSellerNumber(sellerDto.getSellerNumber());
        sellerService.insertSellerOrder(sellerOrderDto, materialNumber);


        return "redirect:/seller/orderDetailPage";
    }


    @RequestMapping("orderDetailPage")
    public String orderDetailPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();

        List<Map<String, Object>> sellerOrderList = sellerService.selectSellerOrder(sellerNumber);

        model.addAttribute("sellerOrderList", sellerOrderList);


        return "seller/orderDetailPage";
    }


    @RequestMapping("deleteSellerRecentOrder")
    public String deleteSellerRecentOrder() {
        sellerService.deleteSellerRecentOrder();

        return "redirect:/seller/orderPage";
    }


    @RequestMapping("updateMaterialQuantity")
    public String updateMaterialQuantity(int[] sellerOrderQuantity, int[] sellerOrderNumber) {

        sellerService.updateMaterialQuantity(sellerOrderQuantity, sellerOrderNumber);

        return "redirect:/seller/orderSuccessPage";
    }



    @RequestMapping("orderSuccessPage")
    public String orderSuccessPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();

        List<Map<String, Object>> sellerOrderList = sellerService.selectSellerOrder(sellerNumber);

        model.addAttribute("sellerOrderList", sellerOrderList);

        return "seller/orderSuccessPage";
    }


    @RequestMapping("materialMenuPage1")
    public String materialMenuPage1() {
        return "/seller/materialMenuPage1";
    }

    @RequestMapping("materialMenuPage2")
    public String materialMenuPage2() {
        return "/seller/materialMenuPage2";
    }

    @RequestMapping("materialMenuPage3")
    public String materialMenuPage3() {
        return "/seller/materialMenuPage3";
    }

    @RequestMapping("materialMenuPage5")
    public String materialMenuPage5() {
        return "/seller/materialMenuPage5";
    }

    @RequestMapping("materialMenuPage6")
    public String materialMenuPage6() {
        return "/seller/materialMenuPage6";
    }


    @RequestMapping("materialMenuPage4")
    public String materialMenuPage4() {
        return "/seller/materialMenuPage4";
    }


    @RequestMapping("sellerReviewPage")
    public String sellerReviewPage(Model model) {

        List<Map<String, Object>> reviewInform = sellerService.selectAllSellerReview();
        model.addAttribute("reviewInform", reviewInform);

        return "/seller/sellerReviewPage";
    }


    @RequestMapping("myInformPage")
    public String myInformPage() {
        return "/seller/myInformPage";
    }


    @RequestMapping("orderCheckPage")
    public String orderCheckPage(HttpSession session, Model model) {
        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();
        List<Map<String, Object>> sellerOrderList = sellerService.selectAllSellerOrder(sellerNumber);
        model.addAttribute("sellerOrderList", sellerOrderList);

        return "/seller/orderCheckPage";
    }


    @RequestMapping("orderDetailCheckPage")
    public String orderDetailCheckPage(Model model, SellerOrderDto sellerOrderDto, int id) {

        List<Map<String, Object>> sellerOrderList = sellerService.selectSameSellerOrder(sellerOrderDto, id);
        model.addAttribute("sellerOrderList", sellerOrderList);

        return "/seller/orderDetailCheckPage";
    }


    @RequestMapping("reviewChoicePage")
    public String reviewChoicePage(HttpSession session, Model model) {



        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();
        List<Map<String, Object>> sellerOrderList = sellerService.selectAllSellerOrder(sellerNumber);
        model.addAttribute("sellerOrderList", sellerOrderList);

        return "/seller/reviewChoicePage";
    }


    @RequestMapping("reviewWritePage")
    public String reviewWritePage(Model model, SellerOrderDto sellerOrderDto, int id) {

        SellerOrderDto sellerOrderInform = sellerService.selectSellerOrderInform(id);
        List<Map<String, Object>> sellerOrderList = sellerService.selectSameSellerOrder(sellerOrderDto, id);
        model.addAttribute("sellerOrderList", sellerOrderList);
        model.addAttribute("sellerOrderInform", sellerOrderInform);

        return "/seller/reviewWritePage";
    }


    @RequestMapping("insertSellerReview")
    public String insertSellerReview(SellerReviewDto sellerReviewDto, MultipartFile[] uploadFiles) {

        List<SellerReviewImageDto> sellerReviewImageDtoList = new ArrayList<>();

        if (uploadFiles != null) {
            for (MultipartFile multipartFile : uploadFiles) {
                if (multipartFile.isEmpty()) {
                    continue;
                }

                String rootPath = "/Users/fiveguys_image/";

                //날짜 별 폴더 생성

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy//MM/dd/");
                String todayPath = sdf.format(new Date());

                File todayFolderForCreate = new File(rootPath + todayPath);

                if (!todayFolderForCreate.exists()) {
                    todayFolderForCreate.mkdirs();
                }

                String originalFilename = multipartFile.getOriginalFilename();

                String uuid = UUID.randomUUID().toString();
                long currentTime = System.currentTimeMillis();

                String fileName = uuid + "_" + currentTime;

                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                fileName += ext;

                try{
                    multipartFile.transferTo(new File(rootPath + todayPath + fileName));
                }catch (Exception e){
                    e.printStackTrace();
                }

                SellerReviewImageDto sellerReviewImageDto = new SellerReviewImageDto();
                sellerReviewImageDto.setOriginal_filename(originalFilename);
                sellerReviewImageDto.setLocation(todayPath + fileName);

                sellerReviewImageDtoList.add(sellerReviewImageDto);

            }
        }

        sellerService.insertSellerReview(sellerReviewDto, sellerReviewImageDtoList);

        return "/seller/mainPage";
    }



    @RequestMapping("reviewDetailPage")
    public String reviewDetailPage(Model model, int id) {



        Map<String, Object> reviewInform = sellerService.selectSellerReview(id);
        model.addAttribute("reviewInform", reviewInform);

        return "/seller/reviewDetailPage";
    }



    @RequestMapping("myReviewPage")
    public String myReviewPage(HttpSession session, Model model) {

        SellerDto sellerDto = (SellerDto)session.getAttribute("sellerDto");
        int sellerNumber = sellerDto.getSellerNumber();
        List<Map<String, Object>> reviewInform = sellerService.selectMyReview(sellerNumber);

        model.addAttribute("reviewInform", reviewInform);

        return "/seller/myReviewPage";
    }





}
