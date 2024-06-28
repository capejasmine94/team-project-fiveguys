package com.fiveguys.login.controller;

import com.fiveguys.customer.service.CommunityService;
import com.fiveguys.dto.CommunityDto;
import com.fiveguys.dto.CustomerDto;
import com.fiveguys.dto.EventBoardDto;
import com.fiveguys.dto.MasterDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.login.service.LoginService;
import com.fiveguys.master.service.EventService;

import java.util.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("customerLogin")
    public String customerLogin() {
        return "login/customerLogin";
    }
    @RequestMapping("sellerLogin")
    public String sellerLogin() {
        return "login/sellerLogin";
    }

    @RequestMapping("masterLogin")
    public String masterLogin() {
        return "login/masterLogin";
    }

    @RequestMapping("customerLogout")
    public String customerLogout() {
        return "login/customerLogout";
    }

    @RequestMapping("customerLoginProcess")
    public String customerLoginProcess(HttpSession session, @ModelAttribute CustomerDto customerDto, Model model) {
        CustomerDto customerDto1 = loginService.selectCustomerCheck(customerDto);
        if (customerDto1 == null) {
            return "redirect:./customerLogin";
        }else{
            session.setAttribute("customerDto", customerDto1);
        }
        return "redirect:/customer/mainPage";

    }
    @RequestMapping("sellerLoginProcess")
    public String sellerLoginProcess(HttpSession session, @ModelAttribute SellerDto sellerDto) {
        SellerDto sellerDto1 = loginService.selectSellerCheck(sellerDto);
        if (sellerDto1 == null) {
            return "redirect:./sellerLogin";
        }else{
            session.setAttribute("sellerDto", sellerDto1);
        }
        return "redirect:/seller/mainPage";

    }
    @RequestMapping("masterLoginProcess")
    public String masterLoginProcess(HttpSession session, @ModelAttribute MasterDto masterDto) {
        MasterDto masterDto1 = loginService.selectMasterCheck(masterDto);
        if (masterDto1 == null) {
            return "redirect:./masterLogin";
        }else{
            session.setAttribute("masterDto", masterDto1);
        }
        return "redirect:/master/mainPage";
    }

    @RequestMapping("customerLogoutProcess")
    public String customerLogoutProcess(HttpSession session) {

        session.invalidate();
        return "redirect:/customer/mainPage";
    }

    @RequestMapping("sellerLogoutProcess")
    public String sellerLogoutProcess(HttpSession session) {

        session.invalidate();
        return "redirect:/seller/mainPage";
    }

    @RequestMapping("masterLogoutProcess")
    public String masterLogoutProcess(HttpSession session) {

        session.invalidate();
        return "redirect:/master/mainPage";
    }
}
