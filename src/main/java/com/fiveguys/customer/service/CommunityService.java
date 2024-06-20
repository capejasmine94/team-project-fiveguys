package com.fiveguys.customer.service;

import com.fiveguys.customer.mapper.CommunitySqlMapper;
import com.fiveguys.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class CommunityService {

    @Autowired
    private CommunitySqlMapper communitySqlMapper;

    public void insertCommunityWrite(CommunityDto params, List<CommunityDetailImageDto> communityDetailImageDtoList) {
        communitySqlMapper.insertCommunityWrite(params);
        for(CommunityDetailImageDto communityDetailImageDto : communityDetailImageDtoList){
//            System.out.println(params.getCommunityNumber() + "넘버");
            communityDetailImageDto.setCommunityId(params.getCommunityNumber());

            communitySqlMapper.communityDetailImageDtoList(communityDetailImageDto);
        }

    }

    public List<Map<String, Object>> selectCommunityList() {
        List<Map<String, Object>> result = new ArrayList<>();

        List<CommunityDto> communityList = communitySqlMapper.selectCommunityList();

        for (CommunityDto communityDto : communityList) {
            int writerPk = communityDto.getCustomerNumber();
            CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(writerPk);

            //좋아요 카운트
            int communiyNumber = communityDto.getCommunityNumber();
            int likeCount = communitySqlMapper.selectCountCommunityLike(communiyNumber);

            //댓글 카운트
            int commentCount = communitySqlMapper.selectCountCommentNumber(communiyNumber);

            //이미지 처리
            List<CommunityDetailImageDto> communityImageDtoFile = communitySqlMapper.selectCommunityDatailImageDtoList(communiyNumber);

            Map<String, Object> map = new HashMap<>();

            map.put("communityDto", communityDto);
            map.put("customerDto", customerDto);
            map.put("likeCount", likeCount);
            map.put("commentCount", commentCount);
            map.put("communityImageDtoFile", communityImageDtoFile);

            System.out.println("이미지파일서비스서비스서시버삳기" + communityImageDtoFile);

            result.add(map);
        }

        return result;
    }

    public Map<String, Object> selectCommunityNumber(int communityNumber) {
        Map<String, Object> map = new HashMap<>();

        CommunityDto communityDto = communitySqlMapper.selectCommunityNumber(communityNumber);
        CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(communityDto.getCustomerNumber());

        map.put("communityDto", communityDto);
        map.put("customerDto", customerDto);

        return map;
    }

    public void updateVisitCount(int communityNumber) {
        communitySqlMapper.updateVisitCount(communityNumber);
    }

    public void deleteCommunityPage(int communityNumber) {
        communitySqlMapper.deleteCommunityPage(communityNumber);
    }

    public void updateCommunityPage(CommunityDto communityDto) {
        communitySqlMapper.updateCommunityPage(communityDto);
    }

    //댓글
    public void insertCommunityComment(CommunityCommentDto communityCommentDto) {
        communitySqlMapper.insertCommunityComment(communityCommentDto);
    }

    //댓글 리스트 출력
    public List<Map<String, Object>> selectCommunityCommentList(int communityNumber) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<CommunityCommentDto> communityCommentDtoList = communitySqlMapper.selectCommunityCommentList(communityNumber);

        for (CommunityCommentDto communityCommentDto : communityCommentDtoList) {
            int CommentWriterPk = communityCommentDto.getCustomerNumber();
            CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(CommentWriterPk);

            Map<String, Object> map = new HashMap<>();

            map.put("communityCommentDto", communityCommentDto);
            map.put("customerDto", customerDto);

            result.add(map);
        }

        return result;
    }

    // 좋아요
    public void insertCommunityLike(CommunityLikeDto communityLikeDto){
        communitySqlMapper.insertCommunityLike(communityLikeDto);
    }

    public void deleteLikeNumber(int LikeNumber){
        communitySqlMapper.deleteLikeNumber(LikeNumber);
    }

    public CommunityLikeDto selectCommunityLike(CommunityLikeDto communityLikeDto){
        return communitySqlMapper.selectCommunityLike(communityLikeDto);
    }

    public int selectCountCommunityLike(int communityNumber){
        return communitySqlMapper.selectCountCommunityLike(communityNumber);
    }

    //이미지
    public List<CommunityDetailImageDto> selectCommunityDatailImageDtoList(int cummunityId){
        return communitySqlMapper.selectCommunityDatailImageDtoList(cummunityId);

    }




}









