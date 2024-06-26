package com.fiveguys.master.controller;

import com.fiveguys.dto.*;
import com.fiveguys.master.service.MasterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @RequestMapping("mainPage")
    public String mainPage(Model model) {

        List<Map<String, Object>> sellerReviewInform = masterService.selectRecentSellerReview();
        List<Map<String, Object>> sellerOrderInform = masterService.selectRecentSellerOrder();
        model.addAttribute("sellerReviewInform", sellerReviewInform);
        model.addAttribute("sellerOrderInform", sellerOrderInform);

        return "/master/mainPage";
    }


    @RequestMapping("logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login/masterLogin";
    }


    @RequestMapping("reviewPage")
    public String reviewPage(Model model) {

        List<Map<String, Object>> sellerReviewList = masterService.selectAllSellerReview();
        model.addAttribute("sellerReviewList", sellerReviewList);

        return "/master/reviewPage";
    }


    @RequestMapping("reviewDetailPage")
    public String reviewDetailPage(Model model, int id) {

        Map<String, Object> reviewInform =  masterService.selectSellerReview(id);
        model.addAttribute("reviewInform", reviewInform);

        return "/master/reviewDetailPage";
    }


    @RequestMapping("insertMasterReply")
    public String insertMasterReply(MasterReplyDto masterReplyDto) {
        masterService.insertMasterReply(masterReplyDto);

        SellerReviewDto sellerReviewDto = masterService.selectSellerReviewInformByReviewNumber(masterReplyDto.getSellerReviewNumber());

        return "redirect:/master/reviewDetailPage?id=" + sellerReviewDto.getSellerOrderNumber();
    }


    @RequestMapping("deleteReply")
    public String deleteReply(int masterReplyNumber, int sellerOrderNumber) {
        masterService.deleteReply(masterReplyNumber);

        return "redirect:/master/reviewDetailPage?id=" + sellerOrderNumber;
    }


    @RequestMapping("updateReply")
    public String updateReply(MasterReplyDto masterReplyDto, int sellerOrderNumber) {

        masterService.updateReply(masterReplyDto);

        return "redirect:/master/reviewDetailPage?id=" + sellerOrderNumber;
    }



    @RequestMapping("replyUpdatePage")
    public String replyUpdatePage(Model model, int id) {

        Map<String, Object> reviewInform =  masterService.selectSellerReview(id);
        model.addAttribute("reviewInform", reviewInform);

        return "/master/replyUpdatePage";
    }


    @RequestMapping("orderPage")
    public String orderPage(Model model, int id) {
        List<Map<String, Object>> sellerOrderInform = masterService.selectAllSellerOrder(id);

        model.addAttribute("sellerOrderInform", sellerOrderInform);

        return "/master/orderPage";
    }


    @RequestMapping("orderDetailPage")
    public String orderDetailPage(Model model, SellerOrderDto sellerOrderDto, int id) {


        SellerOrderDto sellerOrderDtos = masterService.selectSellerOrder(id);
        int totalPrice = masterService.selectOrderTotalPrice(id);

        List<Map<String, Object>> sellerOrderInform = masterService.selectSameSellerOrder(sellerOrderDto, id);

        model.addAttribute("sellerOrderInform", sellerOrderInform);
        model.addAttribute("sellerOrderDto", sellerOrderDtos);
        model.addAttribute("totalPrice", totalPrice);

        return "/master/orderDetailPage";
    }



    @RequestMapping("updateOrderStatusProcessingShipment")
    public String updateOrderStatusProcessingShipment(@RequestParam("id") int id) {

        masterService.updateOrderStatusProcessingShipment(id);

        return "redirect:/master/orderDetailPage?id=" + id;
    }

    @RequestMapping("updateOrderStatusDelivery")
    public String updateOrderStatusDelivery(@RequestParam("id") int id) {

        masterService.updateOrderStatusDelivery(id);

        return "redirect:/master/orderDetailPage?id=" + id;
    }


    @RequestMapping("updateOrderStatusDeliveryCompleted")
    public String updateOrderStatusDeliveryCompleted(@RequestParam("id") int id) {

        masterService.updateOrderStatusDeliveryCompleted(id);

        return "redirect:/master/orderDetailPage?id=" + id;
    }


    @RequestMapping("materialRegisterPage")
    public String materialRegisterPage(Model model) {

        List<MaterialCategoryDto> materialCategoryInform = masterService.selectMaterialCategory();

        model.addAttribute("materialCategoryInform", materialCategoryInform);

        return "/master/materialRegisterPage";
    }




    @RequestMapping("materialRegisterSuccessPage")
    public String materialRegisterSuccessPage(Model model) {

        Map<String, Object> materialInform = masterService.selectRecentMaterial();
        model.addAttribute("materialInform", materialInform);

        return "/master/materialRegisterSuccessPage";
    }






    @RequestMapping("materialCategoryRegisterPage")
    public String materialCategoryRegisterPage() {

        return "/master/materialCategoryRegisterPage";
    }



    @RequestMapping("insertMaterialCategory")
    public String insertMaterialCategory(String materialCategoryName) {

        masterService.insertMaterialCategory(materialCategoryName);

        return "redirect:/master/materialCategoryRegisterPage";
    }


    @RequestMapping("insertMaterial")
    public String insertMaterial(MaterialDto materialDto, @RequestParam("uploadFile") MultipartFile uploadFile) {



        if (uploadFile != null) {
            if(uploadFile.isEmpty()) {

                return "/master/materialRegisterFailPage";

            }

            String rootPath = "C:/fiveguys_image/";

            //날짜별 폴더생성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy//MM/dd/");
            String todayPath = sdf.format(new Date());

            File todayFolderForCreate = new File(rootPath + todayPath);

            if (!todayFolderForCreate.exists()) {
                todayFolderForCreate.mkdirs();
            }

            String originalFilename = uploadFile.getOriginalFilename();

            String uuid = UUID.randomUUID().toString();
            long currentTime = System.currentTimeMillis();

            String filename = uuid + "_" + currentTime;

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            filename += ext;

            try{
                uploadFile.transferTo(new File(rootPath + todayPath + filename));
            }catch (Exception e){
                e.printStackTrace();
            }

            MaterialImageDto materialImageDto = new MaterialImageDto();
            materialImageDto.setOriginal_filename(originalFilename);
            materialImageDto.setLocation(todayPath + filename);

            masterService.insertMaterial(materialDto, materialImageDto);

        }




        return "redirect:/master/materialRegisterSuccessPage";
    }




}
