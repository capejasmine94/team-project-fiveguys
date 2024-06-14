package com.fiveguys.customer.service;

import com.fiveguys.customer.mapper.CommunitySqlMapper;
import com.fiveguys.dto.CommunityDto;
import com.fiveguys.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunityService {

    @Autowired
    private CommunitySqlMapper communitySqlMapper;

    public void insertCommunity(CommunityDto communityDto){
        communitySqlMapper.insertCommunity(communityDto);
    }

    public List<Map<String, Object>> selectCommunityList(){
        List<CommunityDto> communityList = communitySqlMapper.selectCommunityList();

        List<Map<String, Object>> result = new ArrayList<>();

        for(CommunityDto communityDto : communityList) {
            int writerPk = communitySqlMapper.selectCustomerNumber(communityDto.getCustomerNumber());
            CustomerDto customerDto = communitySqlMapper.selectCommunityNumber(writerPk);

            Map<String, Object> map = new HashMap<>();

            map.put("communityDto", communityDto);
            map.put("customerDto", customerDto);

            result.add(map);
        }
        return result;
    }

//    public Map<String, Object> selectCommunityNumber(int customerNumber){
//        Map<String, Object> map = new HashMap<>();
//
//        CommunityDto communityDto = communitySqlMapper.selectCommunityNumber(communityNumber);
//        CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(communityDto.getCustomerNumber());  ;
//
//        map.put("communityDto", communityDto);
//        map.put("customerDto", customerDto);
//
//        return map;
//    }








}
