package com.fiveguys.customer.service;

import com.fiveguys.customer.mapper.CommunitySqlMapper;
import com.fiveguys.dto.CommunityCommentDto;
import com.fiveguys.dto.CommunityDto;
import com.fiveguys.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommunityService {

    @Autowired
    private CommunitySqlMapper communitySqlMapper;

    public void insertCommunityWrite(CommunityDto params){
        communitySqlMapper.insertCommunityWrite(params);
    }

    public List<Map<String, Object>> selectCommunityList(){
        List<Map<String, Object>> result = new ArrayList<>();

        List<CommunityDto> communityList = communitySqlMapper.selectCommunityList();

        for(CommunityDto communityDto : communityList) {
            int writerPk = communityDto.getCustomerNumber();
            CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(writerPk);

            Map<String, Object> map = new HashMap<>();

            map.put("communityDto", communityDto);
            map.put("customerDto", customerDto);

            result.add(map);
        }

        return result;
    }

    public Map<String, Object> selectCommunityNumber(int communityNumber){
        Map<String, Object> map = new HashMap<>();

        CommunityDto communityDto = communitySqlMapper.selectCommunityNumber(communityNumber);
        CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(communityDto.getCustomerNumber());

        map.put("communityDto", communityDto);
        map.put("customerDto", customerDto);

        return map;
    }

    public void updateVisitCount(int communityNumber){
        communitySqlMapper.updateVisitCount(communityNumber);
    }

    public void deleteCommunityPage(int communityNumber){
        communitySqlMapper.deleteCommunityPage(communityNumber);
    }

    public void updateCommunityPage(CommunityDto communityDto){
        communitySqlMapper.updateCommunityPage(communityDto);
    }

    //댓글
    public void insertCommunityComment(CommunityCommentDto communityCommentDto){
        communitySqlMapper.insertCommunityComment(communityCommentDto);
    }










}
