package com.fiveguys.customer.mapper;

import com.fiveguys.dto.CommunityDto;
import com.fiveguys.dto.CustomerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

@Mapper
public interface CommunitySqlMapper {

    public void insertCommunity(CommunityDto communityDto);
    public List<CommunityDto> selectCommunityList();
    public CustomerDto selectCustomerNumber(int selectCustomerNumber);


}
