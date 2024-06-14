package com.fiveguys.seller.mapper;

import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface SellerSqlMapper {

    public List<SellerDto> selectAllSellers();

    public SellerDto sellerLoginProcess(SellerDto sellerDto);

}
