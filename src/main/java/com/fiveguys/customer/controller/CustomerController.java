package com.fiveguys.customer.controller;

import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.service.SellerCustomerService;
import com.fiveguys.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    public String storesDetailPage(@RequestParam("sellerNumber") int sellerNumber, Model model) {

        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
        model.addAttribute("sellerDto", sellerDto);

        return "customer/storesDetailPage";
    }
}
