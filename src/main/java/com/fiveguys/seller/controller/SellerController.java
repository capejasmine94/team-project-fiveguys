package com.fiveguys.seller.controller;

import com.fiveguys.dto.*;
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
    public String orderPage(Model model) {

        List<MaterialDto> materialInform = sellerService.selectMaterial();

        model.addAttribute("materialInform", materialInform);

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


    @RequestMapping("materialMenuPage")
    public String materialMenuPage(Model model) {

        List<MaterialCategoryDto> materialCategoryInform = sellerService.selectMaterialCategory();

        model.addAttribute("materialCategoryInform", materialCategoryInform);

        return "/seller/materialMenuPage";
    }


    @RequestMapping("selectMaterialByCategoryNumber")
    public String selectMaterialByCategoryNumber(@RequestParam("materialCategoryNumber") int materialCategoryNumber, Model model) {

        List<Map<String, Object>> materialInform = sellerService.selectMaterialByCategoryNumber(materialCategoryNumber);
        List<MaterialCategoryDto> materialCategoryInform = sellerService.selectMaterialCategory();


        model.addAttribute("materialInform", materialInform);
        model.addAttribute("materialCategoryInform", materialCategoryInform);

        return "/seller/selectMaterialByCategoryNumber";
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

        return "redirect:/seller/reviewDetailPage?id=" + sellerService.selectRecentReviewNumber();
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
