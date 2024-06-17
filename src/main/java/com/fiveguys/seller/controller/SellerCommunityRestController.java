package com.fiveguys.seller.controller;

import com.fiveguys.dto.SellerCommunityDto;
import com.fiveguys.dto.SellerCommunityLikeDto;
import com.fiveguys.dto.SellerCommunityPaginationDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.service.SellerCommunityService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/seller")
public class SellerCommunityRestController {

    @Autowired
    SellerCommunityService sellerCommunityService;

    @RequestMapping("getSellerCommunityList")
    public Map<String, Object> getSellerCommunityList(HttpSession session, SellerCommunityPaginationDto sellerCommunityPaginationDto) {
        Map<String, Object> result = new HashMap<String, Object>();

        //페이지네이션 처리
        int totalPage = sellerCommunityService.selectSellerCommunityCount();

        int lastPageNumber=0;

        if(totalPage % sellerCommunityPaginationDto.getItemsPerPage()==0){
            lastPageNumber = (int)Math.ceil((double)totalPage/sellerCommunityPaginationDto.getItemsPerPage())-1;
        }else{
            lastPageNumber = (int)Math.ceil((double)totalPage/sellerCommunityPaginationDto.getItemsPerPage());
        }

        int startPage =((sellerCommunityPaginationDto.getCurrentPage()-1)/5)*5+1;
        int endPage =((sellerCommunityPaginationDto.getCurrentPage()-1)/5+1)*5;

        if(endPage>lastPageNumber){
            endPage = lastPageNumber;
        }


        sellerCommunityPaginationDto.setStartPage(startPage);
        sellerCommunityPaginationDto.setEndPage(endPage);
        sellerCommunityPaginationDto.setPaginationPage(lastPageNumber);

        result.put("sellerCommunityPaginationDto", sellerCommunityPaginationDto);

        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        result.put("sellerCommunity", sellerCommunityService.selectSellerCommunityList(sellerCommunityPaginationDto,sellerDto.getSellerNumber()));

        return result;
    }

    @RequestMapping("sellerCommunityLike")
    public Map<String, Object> sellerCommunityLike(HttpSession session, SellerCommunityLikeDto sellerCommunityLikeDto) {
        Map<String, Object> response = new HashMap<>();
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");
        if(sellerDto==null){
            response.put("success", false);
        }else{
            sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());
            int checkIfSellerCommunityLikeExists = sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);
            if(checkIfSellerCommunityLikeExists==0){
                sellerCommunityService.insertSellerCommunityLike(sellerCommunityLikeDto);
            }else {
                sellerCommunityService.deleteSellerCommunityLike(sellerCommunityLikeDto);

            }
            response.put("success", true);
        }
        return response;
    }

    @RequestMapping("reloadSellerCommunityLike")
    public Map<String,Object> reloadSellerCommunityLike(HttpSession session,int[] sellerCommunityNumber){
        Map<String,Object> response = new HashMap<>();
        SellerDto sellerDto = (SellerDto) session.getAttribute("sellerDto");

        if(sellerDto==null) {
            response.put("success", false);
        }else{
            for(int i=0;i<sellerCommunityNumber.length;i++){
                SellerCommunityLikeDto sellerCommunityLikeDto = new SellerCommunityLikeDto();
                sellerCommunityLikeDto.setSellerNumber(sellerDto.getSellerNumber());
                sellerCommunityLikeDto.setSellerCommunityNumber(sellerCommunityNumber[i]);

                int checkIfSellerCommunityLikeExists = sellerCommunityService.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);

                if(checkIfSellerCommunityLikeExists!=0){
                    response.put("heartFill",true);
                }else{
                    response.put("heartFill",false);
                }
            }
        }
        return response;
    }


}
