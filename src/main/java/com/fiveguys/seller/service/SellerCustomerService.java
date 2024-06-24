package com.fiveguys.seller.service;

import com.fiveguys.dto.*;
import com.fiveguys.seller.mapper.SellerCustomerSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<ProductCategoryDto> selectProductCategoryNameList() {

        return sellerCustomerSqlMapper.selectProductCategoryNameList();
    }

    public ProductCategoryDto selectProductCategoryByNumber(int productCategoryNumber) {

        return sellerCustomerSqlMapper.selectProductCategoryByNumber(productCategoryNumber);
    }

    public List<Map<String,Object>> selectProductList(int productCategoryNumber) {

        List<Map<String,Object>> result = new ArrayList<>();

        List<ProductDto> list = sellerCustomerSqlMapper.selectProductList(productCategoryNumber);
        for (ProductDto productDto : list) {
            int categoryPk = productDto.getProductCategoryNumber();
            ProductCategoryDto productCategoryDto = sellerCustomerSqlMapper.selectProductCategoryByNumber(categoryPk);

            Map<String,Object> map = new HashMap<>();
            map.put("productCategoryDto", productCategoryDto);
            map.put("productDto", productDto);

            result.add(map);
        }
        return result;
    }

    public ProductDto selectProductDetailMenu(int productNumber) {

        return sellerCustomerSqlMapper.selectProductDetailMenu(productNumber);
    }

    // 메뉴옵션
    public Map<String, List<ProductOptionsWithValuesDto>> selectProductOptionsWithValuesList(int productNumber) {

        Map<String, List<ProductOptionsWithValuesDto>> result = new HashMap<>();

        List<ProductOptionsWithValuesDto> list = sellerCustomerSqlMapper.selectProductOptionsWithValuesList(productNumber);
        List<String> filteredList = list.stream() // 필터링메서드
                .map(ProductOptionsWithValuesDto::getOptionName)  //
                .distinct().toList();
        // .distinct() 중복제거

        for(String optionName : filteredList){
            List<ProductOptionsWithValuesDto> productOptionsWithValuesList = list.stream()
                    .filter(f -> f.getOptionName().equals(optionName))
                    .toList();
            result.put(optionName, productOptionsWithValuesList);
        }
        return result;
    }
    // 메뉴 사이드 옵션
    public Map<String, List<ProductCategoryJoinDto>> selectProductCategoryJoinList(List<Integer> productCategoryNumbers) {
        // 쿼리에서 받은대로 순서대로 정렬하려면 LinkedHashMap<>(); 사용 hashMap은 랜덤으로 출력
        Map<String, List<ProductCategoryJoinDto>> result = new LinkedHashMap<>();

        List<ProductCategoryJoinDto> list = sellerCustomerSqlMapper.selectProductCategoryJoinList(productCategoryNumbers);
        List<String> filteredList = list.stream() //
                .map(ProductCategoryJoinDto::getProductCategoryName)  //
                .distinct().toList();

        for(String categoryName : filteredList){
            List<ProductCategoryJoinDto> productOptionsWithValuesList = list.stream()
                    .filter(f -> f.getProductCategoryName().equals(categoryName))
                    .collect(Collectors.toList());
            result.put(categoryName, productOptionsWithValuesList);
        }

        return result;
    }

    // 장바구니 추가
    public void insertOrderMenu(OrderMenuDto orderMenuDto) {
        sellerCustomerSqlMapper.insertOrderMenu(orderMenuDto);
    }
//    // 장바구니 메뉴 중복 체크
//    public OrderMenuDto selectOrderMenuDuplicateCheck(int productNumber) {
//        return sellerCustomerSqlMapper.selectOrderMenuDuplicateCheck(productNumber);
//    }
//    // 장바구니 메뉴 리스트
//    public List<OrderMenuProductDto> selectOrderMenuList() {
//        return sellerCustomerSqlMapper.selectOrderMenuList();
//    }
//    // 장바구니 메뉴 삭제
//    public void deleteOrderMenu(int customerOrderNumber) {
//        sellerCustomerSqlMapper.deleteOrderMenu(customerOrderNumber);
//    }
//    // 장바구니 메뉴 수량 변경
//    public void updateOrderMenuQuantity(OrderMenuQuantityUpdateDto updateDto) {
//        sellerCustomerSqlMapper.updateOrderMenuQuantity(updateDto);
//    }

    // 주소등록
    public void insertCustomerAddress(CustomerAddressDto customerAddressDto) {
        sellerCustomerSqlMapper.insertCustomerAddress(customerAddressDto);
    }
    // 주소리스트
    public List<CustomerAddressDto> selectCustomerAddressList(int customerNumber) {
        return sellerCustomerSqlMapper.selectCustomerAddressList(customerNumber);
    }
    // 주문 주소 출력
    public String selectCustomerAddress(int customerNumber){
        return sellerCustomerSqlMapper.selectCustomerAddress(customerNumber);
    }














//  메뉴 옵션 나중에 해볼것
//    public List<ProductOptionDto> selectProductOptionList(int productNumber) {
//
//        return sellerCustomerSqlMapper.selectProductOptionList(productNumber);
//    }
//
//    public List<Map<String, Object>> selectProductOptionsWithValues(int productNumber) {
//
//        return sellerCustomerSqlMapper.selectProductOptionsWithValues(productNumber);
//    }
//
//    public Map<String, List<String>> getFormattedProductsByCategories() {
//        List<Map<String, Object>> products = sellerCustomerSqlMapper.getProductsByCategoryNumbers();
//
//        // Use a Map to group product names by category name
//        Map<String, List<String>> categoryProductsMap = new LinkedHashMap<>();
//
//        for (Map<String, Object> product : products) {
//            String category = (String) product.get("productCategoryName");
//            String productName = (String) product.get("productName");
//            categoryProductsMap
//                    .computeIfAbsent(category, k -> new ArrayList<>())
//                    .add(productName);
//        }
//
//        // Sort product names within each category
//        categoryProductsMap.forEach((category, productNames) -> Collections.sort(productNames));
//
//        return categoryProductsMap;
//    }

}
