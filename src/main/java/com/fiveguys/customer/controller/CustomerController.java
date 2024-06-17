package com.fiveguys.customer.controller;

import com.fiveguys.dto.CategoryDto;
import com.fiveguys.dto.ProductAndCategoryDto;
import com.fiveguys.dto.ProductDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.service.SellerCustomerService;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @RequestMapping("menuDetailPage")
    public String customerMenuDetailPage(Model model,
                                         @ModelAttribute ProductDto productDto) {
        try {
            ProductDto productDto1 = sellerCustomerService.selectProductDetail(productDto);
            model.addAttribute("productDto", productDto1);

            List<Integer> categoryNumbers = List.of(5,6,7);

            List<ProductAndCategoryDto> sideMenuList = sellerCustomerService.selectProductAndCategoryByNumber(categoryNumbers);

            // 걸러주는거임
            sideMenuList = sideMenuList.stream()
                    .filter(f -> f.getProductNumber() != productDto.getProductNumber())
                    .collect(Collectors.toList());

            model.addAttribute("sideMenuList", sideMenuList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return "customer/menuDetailPage";
    }

    @RequestMapping("shoppingCartPage")
    public String customerShoppingCartPage() {


        return "customer/shoppingCartPage";
    }
}
