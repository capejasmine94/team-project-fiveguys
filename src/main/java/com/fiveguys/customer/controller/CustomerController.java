package com.fiveguys.customer.controller;

import com.fiveguys.dto.*;
import com.fiveguys.login.service.LoginService;
import com.fiveguys.seller.service.SellerCustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController {

    @Value("#{'${allowed.mainMenuSideList}'.split(',')}")
    private List<Integer> allowedMainMenuSideList;
    @Value("#{'${allowed.sideMenuList}'.split(',')}")
    private List<Integer> allowedSideMenuList;

    @Autowired
    private SellerCustomerService sellerCustomerService;
    @Autowired
    private LoginService loginService;

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
                                   @RequestParam(name = "sellerNumber") int sellerNumber,
                                   @RequestParam(name = "productCategoryNumber", required = false, defaultValue = "0") int productCategoryNumber
    ) {

        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
        model.addAttribute("sellerDto", sellerDto);

        List<ProductCategoryDto> categoryNameList = sellerCustomerService.selectProductCategoryNameList();
        model.addAttribute("categoryNameList", categoryNameList);

        ProductCategoryDto productCategoryDto = sellerCustomerService.selectProductCategoryByNumber(productCategoryNumber);
        model.addAttribute("productCategoryDto", productCategoryDto);

        List<Map<String, Object>> productMenuList = sellerCustomerService.selectProductList(productCategoryNumber);
        model.addAttribute("productMenuList", productMenuList);


        return "customer/storesDetailPage";
    }

    @RequestMapping("menuDetailPage")
    public String menuDetailPage(Model model,
                                 @RequestParam(name = "productNumber") int productNumber,
                                 @RequestParam(name = "productCategoryNumber") int productCategoryNumber) {

        ProductDto productDto = sellerCustomerService.selectProductDetailMenu(productNumber);
        model.addAttribute("productDto", productDto);

        Map<String, List<ProductOptionsWithValuesDto>> optionWithValuesList = sellerCustomerService.selectProductOptionsWithValuesList(productNumber);
        System.out.println(optionWithValuesList);
        model.addAttribute("optionWithValuesList", optionWithValuesList);

        if (allowedSideMenuList.contains(productCategoryNumber)) {
            List<Integer> productCategoryNumbers = allowedMainMenuSideList;
            Map<String, List<ProductCategoryJoinDto>> categoryJoinList = sellerCustomerService.selectProductCategoryJoinList(productCategoryNumbers);
            model.addAttribute("categoryJoinList", categoryJoinList);
        }

//        List<Integer> productCategoryNumbers = allowedMainMenuSideList;
//        Map<String, List<ProductCategoryJoinDto>> categoryJoinList = sellerCustomerService.selectProductCategoryJoinList(productCategoryNumbers);
//        if(productCategoryNumber == 1 || productCategoryNumber == 2 || productCategoryNumber == 3 || productCategoryNumber == 4) {
//            model.addAttribute("categoryJoinList", categoryJoinList);
//        }

        return "customer/menuDetailPage";
    }

    //장바구니 추가
    @RequestMapping("test")
    public String test() {

        return "customer/test";
    }
    @RequestMapping("addToShoppingBasket")
    public String addToShoppingBasket(@ModelAttribute OrderMenuDto orderMenuDto,
                                      @RequestParam(name = "sellerNumber") int sellerNumber,
                                      @RequestParam("productCategoryNumber") int productCategoryNumber) {

        sellerCustomerService.insertOrderMenu(orderMenuDto);

        return "redirect:/customer/shoppingBasketPage?sellerNumber=" + sellerNumber +
                "&productCategoryNumber=" + productCategoryNumber;
    }
//    // 장바구니 리스트
//    @RequestMapping("shoppingBasketPage")
//    public String shoppingBasketPage(Model model,
//                                     @RequestParam(name = "sellerNumber") int sellerNumber,
//                                     @ModelAttribute OrderMenuDto orderMenuDto) {
//
//        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
//        model.addAttribute("sellerDto", sellerDto);
//
//        List<OrderMenuProductDto> selectOrderMenuList = sellerCustomerService.selectOrderMenuList();
//        System.out.println(selectOrderMenuList);
//        model.addAttribute("selectOrderMenuList", selectOrderMenuList);
//
//        return "customer/shoppingBasketPage";
//    }
//
//    @RequestMapping("deleteOrderMenu")
//    public String deleteOrderMenu(@RequestParam("customerOrderNumber") int customerOrderNumber,
//                                  @RequestParam("sellerNumber") int sellerNumber) {
//
//        sellerCustomerService.deleteOrderMenu(customerOrderNumber);
//
//        return "redirect:/customer/shoppingBasketPage?sellerNumber=" + sellerNumber;
//    }
//
//    @RequestMapping("updateOrderMenu")
//    public String updateOrderMenu(@ModelAttribute OrderMenuQuantityUpdateDto updateDto,
//                                  @RequestParam("sellerNumber") int sellerNumber) {
//
//        sellerCustomerService.updateOrderMenuQuantity(updateDto);
//
//        return "redirect:/customer/shoppingBasketPage?sellerNumber=" + sellerNumber;
//    }

    // 더보기 페이지
    @RequestMapping("viewMorePage")
    public String viewMorePage() {

        return "customer/viewMorePage";
    }
    // 더보기 -> 나의정보
    @RequestMapping("customerInformationPage")
    public String customerInformationPage() {


        return "customer/customerInformationPage";
    }
    // 주소 페이지
    @RequestMapping("addressManagementPage")
    public String addressManagementPage(Model model,
                                        @RequestParam("customerNumber") int customerNumber)  {

        List<CustomerAddressDto> customerAddressList = sellerCustomerService.selectCustomerAddressList(customerNumber);
        model.addAttribute("customerAddressList", customerAddressList);

        return "customer/addressManagementPage";
    }
    // 주소 등록
    @RequestMapping("addressManagementProcess")
    public String addressManagementProcess(@ModelAttribute CustomerAddressDto addressDto) {

        sellerCustomerService.insertCustomerAddress(addressDto);

        return "redirect:/customer/addressManagementPage?customerNumber=" + addressDto.getCustomerNumber();
    }

    @RequestMapping("settlementPage")
    public String settlementPage(Model model, HttpSession session,
                                 @RequestParam("sellerNumber") int sellerNumber) {

        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
        model.addAttribute("sellerDto", sellerDto);

        CustomerDto customerNumber = (CustomerDto) session.getAttribute("customerDto");
        String deliveryAddress = sellerCustomerService.selectCustomerAddress(customerNumber.getCustomerNumber());
        model.addAttribute("deliveryAddress", deliveryAddress);

        return "customer/settlementPage";
    }


}

