package com.fiveguys.seller.mapper;

import com.fiveguys.dto.SellerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerSqlMapper {

    public List<SellerDto> selectAllSellers();

    public SellerDto sellerLoginProcess(SellerDto sellerDto);

}
