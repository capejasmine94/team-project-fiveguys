package com.fiveguys.customer.controller;

import com.fiveguys.customer.service.CommunityService;
import com.fiveguys.dto.*;
import com.fiveguys.login.service.LoginService;
import com.fiveguys.master.service.EventService;
import com.fiveguys.seller.service.SellerCustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private SellerCustomerService sellerCustomerService;
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private EventService eventService;
    @Autowired
    private CommunityService communityService;

    @RequestMapping("mainPage")
    public String mainPage(Model model) {
        List<EventBoardDto> eventBoardList = eventService.selectEventBoardLimit();
        List<Map<String,Object>> communityList = communityService.selectCommunityLimit();

        model.addAttribute("eventBoardList", eventBoardList);
        model.addAttribute("communityList", communityList);
        return "customer/mainPage";
    }

    @RequestMapping("myPage")
    public String myPage() {

        return "customer/myPage";
    }

    @RequestMapping("menuPage")
    public String menuPage(Model model) {

        Map<String, List<CustomerMenuDto>> menuCategoryDtoList = sellerCustomerService.MenuPageCategoryList();
        model.addAttribute("menuCategoryDtoList", menuCategoryDtoList);


        return "customer/menuPage";
    }

    @RequestMapping("selectAllStoresPage")
    public String selectAllStoresPage(Model model) {

        List<SellerDto> sellerDtoList = sellerCustomerService.selectAllSellers();
        model.addAttribute("sellerDtoList", sellerDtoList);

        return "customer/selectAllStoresPage";

    }

    @RequestMapping("storesDetailPage")
    public String storesDetailPage(Model model,
                                   @RequestParam(name = "sellerNumber") int sellerNumber) {

        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
        model.addAttribute("sellerDto", sellerDto);

        Map<String, List<CustomerMenuDto>> menuCategoryDtoList = sellerCustomerService.selectMenuCategoryList();
        model.addAttribute("menuCategoryDtoList", menuCategoryDtoList);

        return "customer/storesDetailPage";
    }

    //
    @RequestMapping("menuDetailPage")
    public String menuDetailPage(Model model,
                                 @RequestParam(name = "menuNumber") int menuNumber) {

        // 메뉴 dto
        CustomerMenuDto MenuDetail = sellerCustomerService.selectMenuDetail(menuNumber);
        model.addAttribute("MenuDetail", MenuDetail);
        // 옵션 list
        Map<String, List<CustomerMenuOptionDto>> menuOptionDtoList = sellerCustomerService.selectMenuOptionList(menuNumber);
        model.addAttribute("menuOptionDtoList", menuOptionDtoList);


        return "customer/menuDetailPage";
    }

    @RequestMapping("addToShoppingBasket")
    public String addToShoppingBasket(HttpSession session,
                                      @RequestParam(name = "sellerNumber") int sellerNumber,
                                      @RequestParam(name = "menuNumber") int menuNumber,
                                      @RequestParam(name = "menuOptionNumber") List<Integer> menuOptionNumber) {

        CustomerDto customerDto = (CustomerDto) session.getAttribute("customerDto");

        if (customerDto == null) {
            return "redirect:/login/customerLogin";
        } else {
            Set<Integer> menuOptionSet = new HashSet<>();
            for (Integer menuOptionList : menuOptionNumber) {
                // 중복 체크
                if (!menuOptionSet.contains(menuOptionList)) {
                    CustomerCartDto customerCartDto = new CustomerCartDto();
                    customerCartDto.setMenuOptionNumber(menuOptionList);
                    customerCartDto.setSellerNumber(sellerNumber);
                    customerCartDto.setMenuNumber(menuNumber);
                    customerCartDto.setCustomerNumber(customerDto.getCustomerNumber());
                    // 장바구니 등록
                    sellerCustomerService.insertCustomerCart(customerCartDto);
                    // 중복 방지를 위해 Set 추가
                    menuOptionSet.add(menuOptionList);
                }
            }
        }
        return "redirect:/customer/shoppingBasketPage?sellerNumber=" + sellerNumber;
    }

    @GetMapping("shoppingBasketPage")
    public String shoppingBasketPage(Model model,
                                     HttpSession session,
                                     @RequestParam(name = "sellerNumber") int sellerNumber) {

        SellerDto sellerDto = sellerCustomerService.selectSellersByNumber(sellerNumber);
        model.addAttribute("sellerDto", sellerDto);

        CustomerDto customerDto = (CustomerDto) session.getAttribute("customerDto");

        sellerCustomerService.customerPlaceOrder(customerDto.getCustomerNumber(), sellerNumber);

        Map<String, List<CustomerOrderTotalDto>> selectOrderTotalList = sellerCustomerService.selectOrderTotalList(customerDto.getCustomerNumber());

        System.out.println("selectOrderTotalList: " + selectOrderTotalList);
        model.addAttribute("selectOrderTotalList", selectOrderTotalList);


        return "customer/shoppingBasketPage";
    }

    @RequestMapping("deleteOrderMenu")
    public String deleteOrderMenu(@RequestParam(name = "sellerNumber") int sellerNumber,
                                  @RequestParam(name = "customerOrderNumber") int customerOrderNumber) {

        sellerCustomerService.deleteCustomerOrder(customerOrderNumber);

        return "redirect:/customer/shoppingBasketPage?sellerNumber=" + sellerNumber;
    }
    // 여기서부터 가라
    //더보기 페이지
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
    @RequestMapping("orderHistoryPage")
    public String orderHistoryPage( ) {
        return "customer/orderHistoryPage";
    }
    @RequestMapping("orderDetailHistoryPage")
    public String orderDetailHistoryPage() {
        return "customer/orderDetailHistoryPage";
    }


}



