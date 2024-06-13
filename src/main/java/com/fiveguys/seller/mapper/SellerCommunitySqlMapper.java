package com.fiveguys.seller.mapper;

import com.fiveguys.dto.SellerCommunityDto;
import com.fiveguys.dto.SellerCommunityImageDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SellerCommunitySqlMapper {

    public void insertSellerCommunityWrite(SellerCommunityDto sellerCommunityDto);
    public void insertSellerCommunityImageDetail(SellerCommunityImageDetailDto sellerCommunityImageDetailDto);

    public List<SellerCommunityDto> selectSellerCommunityList();
}
