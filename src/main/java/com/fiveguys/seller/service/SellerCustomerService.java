package com.fiveguys.seller.service;

import com.fiveguys.dto.*;
import com.fiveguys.seller.mapper.SellerCustomerSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerCustomerService {

    @Autowired
    private SellerCustomerSqlMapper sellerCustomerSqlMapper;

    public List<SellerDto> selectAllSellers() {
        return sellerCustomerSqlMapper.selectAllSellers();
    }


    public SellerDto selectSellersByNumber(int sellerNumber) {
        return sellerCustomerSqlMapper.selectSellersByNumber(sellerNumber);
    }

    // 지점 메뉴 카테고리 리스트
    public Map<String, List<CustomerMenuDto>> selectMenuCategoryList() {

        Map<String, List<CustomerMenuDto>> result = new LinkedHashMap<>();

        List<CustomerMenuDto> list = sellerCustomerSqlMapper.selectMenuCategoryList();
        List<String> filteredList = list.stream() //
                .map(CustomerMenuDto::getMenuCategory)  //
                .distinct()
                .limit(4)
                .toList();

        for (String menuCategory : filteredList) {
            List<CustomerMenuDto> customerMenuDtoList = list.stream()
                    .filter(f -> f.getMenuCategory().equals(menuCategory))
                    .toList();
            result.put(menuCategory, customerMenuDtoList);
        }

        return result;
    }

    // 지점 메뉴 상세보기(장바구니 등록전)
    public CustomerMenuDto selectMenuDetail(int menuNumber) {
        return sellerCustomerSqlMapper.selectMenuDetail(menuNumber);
    }

    // 지점 메뉴 상세보기_메인 옵션 리스트
    public Map<String, List<CustomerMenuOptionDto>> selectMenuOptionList(int menuNumber) {

        Map<String, List<CustomerMenuOptionDto>> result = new LinkedHashMap<>();

        List<CustomerMenuOptionDto> list = sellerCustomerSqlMapper.selectMenuOptionList(menuNumber);
        List<String> filteredList = list.stream() //
                .map(CustomerMenuOptionDto::getMenuOptionName)  //
                .distinct().toList();

        for (String menuOptionName : filteredList) {
            List<CustomerMenuOptionDto> customerMenuOptionDtoList = list.stream()
                    .filter(f -> f.getMenuOptionName().equals(menuOptionName))
                    .toList();
            result.put(menuOptionName, customerMenuOptionDtoList);
        }

        return result;
    }

    // 장바구니 등록
    public void insertCustomerCart(CustomerCartDto customerCartDto) {
        sellerCustomerSqlMapper.insertCustomerCart(customerCartDto);
    }
    // 장바구니 삭제
//    public void deleteCustomerCart(int customerNumber){
//        sellerCustomerSqlMapper.deleteCustomerCart(customerNumber);
//    }

    // 메뉴, 메뉴옵션 가격 받기
    public int menuAndMenuOptionPrice(List<CustomerCartDto> customerCartDtoList) {
        int totalPrice = 0;
        for (CustomerCartDto cartDto : customerCartDtoList) {
            int menuOptionNumber = cartDto.getMenuOptionNumber();
            int menuOptionPrice = sellerCustomerSqlMapper.selectMenuOptionPrice(menuOptionNumber);
            totalPrice += menuOptionPrice;
        }
        return totalPrice;
    }

    // 주문
    public void customerPlaceOrder(int customerNumber, int sellerNumber) {
        int totalAmount = 0;
        List<CustomerCartDto> customerCartDtoList = sellerCustomerSqlMapper.selectCustomerCartList(customerNumber);
        // 총 주문 통합가격
        if (!customerCartDtoList.isEmpty()) {
            CustomerCartDto firstCartDto = customerCartDtoList.get(0); // 첫 번째 메뉴만 처리

            totalAmount = sellerCustomerSqlMapper.selectMenuPrice(firstCartDto.getMenuNumber());
        }
        int totalPrice = menuAndMenuOptionPrice(customerCartDtoList);
        totalPrice += totalAmount;
        //구매자주문 등록
//        if(customerCartDtoList.isEmpty()){
//            throw new IllegalStateException("장바구니가 비어있습니다.");
//        }
        CustomerOrderDto customerOrderDto = new CustomerOrderDto();
        customerOrderDto.setCustomerNumber(customerNumber);
        customerOrderDto.setSellerNumber(sellerNumber);
        customerOrderDto.setCustomerOrderTotalPrice(totalPrice);
        sellerCustomerSqlMapper.insertCustomerOrder(customerOrderDto);

        //주문메뉴
        for (CustomerCartDto customerCartDto : customerCartDtoList) {
            CustomerOrderMenuDto customerOrderMenuDto = new CustomerOrderMenuDto();
            customerOrderMenuDto.setCustomerNumber(customerNumber);
            customerOrderMenuDto.setCustomerOrderNumber(customerOrderDto.getCustomerOrderNumber());
            customerOrderMenuDto.setMenuNumber(customerCartDto.getMenuNumber());
            customerOrderMenuDto.setMenuOptionNumber(customerCartDto.getMenuOptionNumber());
            customerOrderMenuDto.setOrderMenuQuantity(customerCartDto.getCartQuantity());
            sellerCustomerSqlMapper.insertCustomerOrderMenu(customerOrderMenuDto);
        }
        // 장바구니 삭제
        sellerCustomerSqlMapper.deleteCustomerCart(customerNumber);
    }

    // 장바구니 통합 리스트 // 중요함
    public Map<String, List<CustomerOrderTotalDto>> selectOrderTotalList(int customerNumber) {

        List<CustomerOrderTotalDto> customerOrderTotalList = sellerCustomerSqlMapper.selectOrderTotalList(customerNumber);

        // 중복 없는 메뉴 이름, 가격, 이미지 조합 리스트 추출
        List<String> filteredList = customerOrderTotalList.stream()
                .map(dto -> dto.getMenuName() + "_" + dto.getMenuPrice() + "_" + dto.getMenuImage() + "_" + dto.getCustomerOrderTotalPrice())
                .distinct()
                .toList();

        // 결과를 담을 맵
        Map<String, List<CustomerOrderTotalDto>> result = new LinkedHashMap<>();
        for (String key : filteredList) {
            List<CustomerOrderTotalDto> filteredOrders = customerOrderTotalList.stream()
                    .filter(dto -> (dto.getMenuName() + "_" + dto.getMenuPrice() + "_" + dto.getMenuImage() + "_" + dto.getCustomerOrderTotalPrice()).equals(key))
                    .toList();
            result.put(key, filteredOrders);
        }

        return result;
    }

    // 장바구니 삭제
    public void deleteCustomerOrder(int customerNumber) {
        sellerCustomerSqlMapper.deleteCustomerOrder(customerNumber);
    }
}

//    // 주소등록
//    public void insertCustomerAddress(CustomerAddressDto customerAddressDto) {
//        sellerCustomerSqlMapper.insertCustomerAddress(customerAddressDto);
//    }
//    // 주소리스트
//    public List<CustomerAddressDto> selectCustomerAddressList(int customerNumber) {
//        return sellerCustomerSqlMapper.selectCustomerAddressList(customerNumber);
//    }
//    // 주문 주소 출력
//    public String selectCustomerAddress(int customerNumber){
//        return sellerCustomerSqlMapper.selectCustomerAddress(customerNumber);
//    }
