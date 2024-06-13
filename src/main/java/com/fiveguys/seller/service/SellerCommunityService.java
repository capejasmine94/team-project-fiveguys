package com.fiveguys.seller.service;

import com.fiveguys.dto.SellerCommunityDto;
import com.fiveguys.dto.SellerCommunityImageDetailDto;
import com.fiveguys.seller.mapper.SellerCommunitySqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerCommunityService {

    @Autowired
    private SellerCommunitySqlMapper sellerCommunitySqlMapper;

    //게시글등록
    public void insertSellerCommunityWrite(SellerCommunityDto sellerCommunityDto, List<SellerCommunityImageDetailDto> sellerCommunityImageDetailDtoList) {
        sellerCommunitySqlMapper.insertSellerCommunityWrite(sellerCommunityDto);
        for (SellerCommunityImageDetailDto sellerCommunityImageDetailDto : sellerCommunityImageDetailDtoList) {
            sellerCommunityImageDetailDto.setSellerCommunityNumber(sellerCommunityDto.getSellerCommunityNumber());
            sellerCommunitySqlMapper.insertSellerCommunityImageDetail(sellerCommunityImageDetailDto);
        }
    }

    public List<SellerCommunityDto> selectSellerCommunityList(){
        return sellerCommunitySqlMapper.selectSellerCommunityList();
    }
}
