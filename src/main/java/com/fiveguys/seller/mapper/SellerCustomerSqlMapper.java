package com.fiveguys.seller.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface SellerCustomerSqlMapper {

    // 매장선택
    public List<SellerDto> selectAllSellers();
    public SellerDto selectSellersByNumber(int sellerNumber);

    public ProductCategoryDto selectProductCategoryByNumber(int productCategoryNumber);

    public List<ProductCategoryDto> selectProductCategoryNameList();
    public List<ProductDto> selectProductList (int productCategoryNumber);

    public ProductDto selectProductDetailMenu(int productNumber);

    // 메뉴 옵션
    public List<ProductOptionsWithValuesDto> selectProductOptionsWithValuesList(int productNumber);
    // 사이드 메뉴 옵션
    public List<ProductCategoryJoinDto> selectProductCategoryJoinList(List<Integer> productCategoryNumbers);

    // 장바구니 추가
    public void insertOrderMenu(OrderMenuDto orderMenuDto);
//    // 장바구니 메뉴 중복 체크
//    public OrderMenuDto selectOrderMenuDuplicateCheck(int productNumber);
//    // 장바구니 메뉴 리스트
//    public List<OrderMenuProductDto> selectOrderMenuList();
//    // 장바구니 메뉴 삭제
//    public void deleteOrderMenu(int customerOrderNumber);
//
//    public void updateOrderMenuQuantity(OrderMenuQuantityUpdateDto updateDto);

    // 주소등록
    public void insertCustomerAddress(CustomerAddressDto customerAddressDto);
    // 주소 리스트
    public List<CustomerAddressDto> selectCustomerAddressList(int customerNumber);
    // 주문 주소 출력
    public String selectCustomerAddress(int customerNumber);



//    메뉴 옵션 나중에 해볼것
//    public List<ProductOptionDto> selectProductOptionList(int productNumber);
//    public List<Map<String, Object>> selectProductOptionsWithValues(int productNumber);
//
//    public List<Map<String, Object>> getProductsByCategoryNumbers();


}
