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
            int communityNumber = communityDto.getCommunityNumber();
            int likeCount = communitySqlMapper.selectCountCommunityLike(communityNumber);

            //댓글 카운트
            int commentCount = communitySqlMapper.selectCountCommentNumber(communityNumber);

            //이미지 처리
            List<CommunityDetailImageDto> communityImageDtoFile = communitySqlMapper.selectCommunityDatailImageDtoList(communityNumber);

            Map<String, Object> map = new HashMap<>();

            map.put("communityDto", communityDto);
            map.put("customerDto", customerDto);
            map.put("likeCount", likeCount);
            map.put("commentCount", commentCount);
            map.put("communityImageDtoFile", communityImageDtoFile);

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
    public List<Map<String, Object>> selectCommunityCommentList(int communityNumber,int customerNumber) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<CommunityCommentDto> communityCommentDtoList = communitySqlMapper.selectCommunityCommentList(communityNumber, customerNumber);
//        List<Map<String, Object>> communityCommentDtoList = communitySqlMapper.selectCommunityCommentList(communityNumber, customerNumber);


        for (CommunityCommentDto communityCommentDto : communityCommentDtoList) {
            Map<String, Object> map = new HashMap<>();

            CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(communityCommentDto.getCustomerNumber());

            int CommentLikeCount =  communitySqlMapper.selectCountCommunityCommentLike(communityCommentDto.getCommentNumber());
            CommentLikeStatusDto commentLikeStatusDto = new CommentLikeStatusDto();
            commentLikeStatusDto.setCustomerNumber(customerNumber);
            commentLikeStatusDto.setCommentNumber(communityCommentDto.getCommentNumber());

            CommentLikeStatusDto commentLikeDto = communitySqlMapper.selectCommentLike(commentLikeStatusDto);

            map.put("commentLikeCount", CommentLikeCount);
            map.put("commentLikeDto", commentLikeDto);
            map.put("communityCommentDto", communityCommentDto);
            map.put("customerDto", customerDto);


//            int CommentWriterPk = (int)communityCommentDto.get("commentNumber");
//            CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(CommentWriterPk);

//            CommentLikeStatusDto commentLikeStatusDto = new CommentLikeStatusDto();
//            commentLikeStatusDto.setCustomerNumber(customerNumber);
//            commentLikeStatusDto.setCommentNumber(communityCommentDto.getCommentNumber());
//
//            CommentLikeStatusDto checkSelectCommunityCommentExists = communitySqlMapper.checkSelectCommunityCommentExists(commentLikeStatusDto);
//            if(checkSelectCommunityCommentExists == null){
//                communitySqlMapper.insertCommentLike(commentLikeStatusDto);
//            }

            //대댓글 리스트
            List<CommunityCommentReplyDto> communityCommentReplyList = communitySqlMapper.selectCommunityCommentReplyList(communityCommentDto.getCommentNumber());

            List<Map<String, Object>> result2 = new ArrayList<>();

            for(CommunityCommentReplyDto communityCommentReply : communityCommentReplyList){
                CustomerDto customerDto1 = communitySqlMapper.selectCustomerNumber(communityCommentReply.getCustomerNumber());

                Map<String, Object> map2 = new HashMap<>();

                map2.put("customerDto1", customerDto1);
                map2.put("communityCommentReply", communityCommentReply);

                result2.add(map2);

            }

            //대댓글 리스트 추가
//            map.put("communityCommentReplyList", communityCommentReplyList);
            map.put("result2", result2);

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

    //댓글 좋아요
    public void insertCommentLike(CommentLikeStatusDto commentLikeStatusDto){
        communitySqlMapper.insertCommentLike(commentLikeStatusDto);
    }
    public void deleteCommentLikeNumber(int commentLikeNumber){
        communitySqlMapper.deleteCommentLikeNumber(commentLikeNumber);
    }
    public CommentLikeStatusDto selectCommentLike(CommentLikeStatusDto commentLikeStatusDto){
        return communitySqlMapper.selectCommentLike(commentLikeStatusDto);
    }

    //댓글 좋아요
//    public List<Map<String, Object>> selectCommentLikeList(int communityNumber){
//        List<Map<String, Object>> commentLikeStatusList = new ArrayList<>();

        //댓글 리스트
       // List<CommunityCommentDto> communityCommentDtoList = communitySqlMapper.selectCommunityCommentList(communityNumber);

        //댓글정보
//        for(CommunityCommentDto communityCommentDto : communityCommentDtoList){
//            Map<String, Object> commentMap = new HashMap<>();
//            int commentNumber = communityCommentDto.getCommentNumber();
//
//            // 댓글 좋아요 상태
//            CommentLikeStatusDto commentLikeStatusDto = new CommentLikeStatusDto();
//            commentLikeStatusDto.setCommentNumber(commentNumber);
//            commentLikeStatusDto.setCustomerNumber(communityCommentDto.getCustomerNumber());
//
//
//            CommentLikeStatusDto commentLikeStatusDto1 = communitySqlMapper.selectCommentLike(commentLikeStatusDto);
//            System.out.println(commentLikeStatusDto1);
//            commentMap.put("commentLikeStatusDto",commentLikeStatusDto1);
//
//            commentMap.put("communityCommentDto",communityCommentDto);
//
//            //댓글 좋아요 카운트
//            int commentLikeCount = communitySqlMapper.selectCountCommunityCommentLike(commentNumber);
//            commentMap.put("commentLikeCount", commentLikeCount);
//
//            commentLikeStatusList.add(commentMap);
//
//        }
//
//        return commentLikeStatusList;
//    }

    //대댓글
    public void insertCommunityCommentReply(CommunityCommentReplyDto communityCommentReplyDto){
        communitySqlMapper.insertCommunityCommentReply(communityCommentReplyDto);
    }

    public List<Map<String, Object>> selectCommunityLimit() {
        List<Map<String, Object>> result = new ArrayList<>();

        List<CommunityDto> communityList = communitySqlMapper.selectCommunityLimit();
        for(CommunityDto communityDto : communityList){
            Map<String,Object> map = new HashMap<>();
            CustomerDto customerDto = communitySqlMapper.selectCustomerNumber(communityDto.getCustomerNumber());

            map.put("communityDto", communityDto);
            map.put("customerDto", customerDto);

            result.add(map);
        }

        return result;
    }

    public void deleteCommunityImage(int communityId){
        communitySqlMapper.deleteCommunityImage(communityId);
    }

    public void insertImageDto(int CommunityNumber, List<CommunityDetailImageDto> communityDetailImageDtoList){
        for(CommunityDetailImageDto communityDetailImageDto : communityDetailImageDtoList){
            communityDetailImageDto.setCommunityId(CommunityNumber);

            communitySqlMapper.communityDetailImageDtoList(communityDetailImageDto);
        }

    }





}









