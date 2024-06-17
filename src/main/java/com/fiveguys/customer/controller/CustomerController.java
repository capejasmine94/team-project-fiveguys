package com.fiveguys.customer.controller;

import com.fiveguys.dto.CategoryDto;
import com.fiveguys.dto.ProductDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.service.SellerCustomerService;
import com.fiveguys.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private SellerCustomerService sellerCustomerService;

    @RequestMapping("mainPage")
    public String mainPage() {
        return "customer/mainPage";
    }

    @RequestMapping("myPage")
    public String myPage() {

        return "customer/myPage";
    }

    @RequestMapping("selectAllStoresPage")
    public String selectAllStoresPage(Model model) {

        List<SellerDto> sellerDtoList = sellerCustomerService.selectAllSellers();
        model.addAttribute("sellerDtoList", sellerDtoList);

        return "customer/selectAllStoresPage";

    }

    @RequestMapping("storesDetailPage")
    public String storesDetailPage(Model model,
                                   @RequestParam("sellerNumber") int sellerNumber,
                                   @RequestParam(value = "categoryNumber", required = false) Integer categoryNumber) {

        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
        model.addAttribute("sellerDto", sellerDto);

        List<CategoryDto> categoryDtoList = sellerCustomerService.selectCategoryList();
        model.addAttribute("categoryDtoList", categoryDtoList);


        if (categoryNumber != null) {
            List<Map<String,Object>> productDtoList = sellerCustomerService.selectProductList(categoryNumber);
            model.addAttribute("productDtoList", productDtoList);
        } else {
            model.addAttribute("productDtoList", Collections.emptyList());
        }

        return "customer/storesDetailPage";
    }


    @RequestMapping("customerMenuDetailPage")
    public String customerMenuDetailPage() {

        return "customer/customerMenuDetailPage";
    }
}
