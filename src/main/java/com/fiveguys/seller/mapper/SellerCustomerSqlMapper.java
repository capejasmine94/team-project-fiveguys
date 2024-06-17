package com.fiveguys.seller.mapper;

import com.fiveguys.dto.CategoryDto;
import com.fiveguys.dto.ProductDto;
import com.fiveguys.dto.SellerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SellerCustomerSqlMapper {

    // 매장선택
    public List<SellerDto> selectAllSellers();
    public SellerDto selectSellersByNumber(int sellerNumber);

    public List<CategoryDto> selectCategoryList();

    public CategoryDto selectCategoryByNumber(int categoryNumber);
    public List<ProductDto> selectProductList(int categoryNumber);

}
