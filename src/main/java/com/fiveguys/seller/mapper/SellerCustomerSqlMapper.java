package com.fiveguys.seller.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SellerCustomerSqlMapper {

    // 매장선택
    public List<SellerDto> selectAllSellers();
    public SellerDto selectSellersByNumber(int sellerNumber);
    // 지점 메뉴 카테고리 리스트
    public List<CustomerMenuDto> selectMenuCategoryList();
    // 지점 메뉴 상세보기(장부구니 등록전)
    public CustomerMenuDto selectMenuDetail(int menuNumber);
    // 지점 메뉴 상세보기_옵션 리스트
    public List<CustomerMenuOptionDto> selectMenuOptionList(int menuNumber);
    // 장바구니 등록
    public void insertCustomerCart(CustomerCartDto customerCartDto);
    // 장바구니 리스트
    public List<CustomerCartDto> selectCustomerCartList(int customerNumber);
    // 장바구니 삭제
    public void deleteCustomerCart(int customerNumber);
    // 구매자 주문
    public void insertCustomerOrder(CustomerOrderDto customerOrderDto);
    // 구매자 주문 메뉴
    public void insertCustomerOrderMenu(CustomerOrderMenuDto customerOrderMenuDto);
    // 통합가격을 위한 메뉴 가격, 메뉴옵션 가격 셀렉
    public int  selectMenuPrice(int menuNumber);
    public int  selectMenuOptionPrice(int menuOptionNumber);
    // 장바구니 통합 리스트
    public List<CustomerOrderTotalDto> selectOrderTotalList(int customerNumber);
    // 장바구니 삭제
    public void deleteCustomerOrder(int customerOrderNumber);
    // 주소등록
    public void insertCustomerAddress(CustomerAddressDto customerAddressDto);
    // 주소 리스트
    public List<CustomerAddressDto> selectCustomerAddressList(int customerNumber);
    // 주문 주소 출력
    public String selectCustomerAddress(int customerNumber);
    // 주문 주소 삭제
    public void deleteCustomerAddress(int customerAddressNumber);


}
