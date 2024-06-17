package com.fiveguys.seller.service;

import com.fiveguys.dto.CategoryDto;
import com.fiveguys.dto.ProductAndCategoryDto;
import com.fiveguys.dto.ProductDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.mapper.SellerCustomerSqlMapper;
import com.fiveguys.seller.mapper.SellerSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<CategoryDto> selectCategoryList() {

        return sellerCustomerSqlMapper.selectCategoryList();
    }

    public List<Map<String,Object>> selectProductList(int categoryNumber) {

        List<Map<String,Object>> result = new ArrayList<>();

        List<ProductDto> list = sellerCustomerSqlMapper.selectProductList(categoryNumber);
        for (ProductDto productDto : list) {
            int categoryPk = productDto.getCategoryNumber();
            CategoryDto categoryDto = sellerCustomerSqlMapper.selectCategoryByNumber(categoryPk);

            Map<String,Object> map = new HashMap<>();
            map.put("categoryDto", categoryDto);
            map.put("productDto", productDto);

            result.add(map);
        }
        return result;
    }

    public ProductDto selectProductDetail(ProductDto productDto) {

        return sellerCustomerSqlMapper.selectProductDetail(productDto);
    }

    public List<ProductAndCategoryDto> selectProductAndCategoryByNumber(List<Integer> categoryNumbers) {

        return sellerCustomerSqlMapper.selectProductsByCategoryNumbers(categoryNumbers);
    }


}
