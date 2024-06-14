package com.fiveguys.seller.service;

import com.fiveguys.dto.*;
import com.fiveguys.seller.mapper.SellerCommunitySqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SellerCommunityService {

    @Autowired
    private SellerCommunitySqlMapper sellerCommunitySqlMapper;

    public SellerDto selectSeller(int sellerNumber){
        return sellerCommunitySqlMapper.selectSeller(sellerNumber);
    }

    //게시글등록
    public void insertSellerCommunityWrite(SellerCommunityDto sellerCommunityDto, List<SellerCommunityImageDetailDto> sellerCommunityImageDetailDtoList) {
        sellerCommunitySqlMapper.insertSellerCommunityWrite(sellerCommunityDto);
        for (SellerCommunityImageDetailDto sellerCommunityImageDetailDto : sellerCommunityImageDetailDtoList) {
            sellerCommunityImageDetailDto.setSellerCommunityNumber(sellerCommunityDto.getSellerCommunityNumber());
            sellerCommunitySqlMapper.insertSellerCommunityImageDetail(sellerCommunityImageDetailDto);
        }
    }

    public List<Map<String,Object>> selectSellerCommunityList(){
        List<Map<String,Object>> result = new ArrayList<>();
        List<SellerCommunityDto> sellerCommunityDtoList = sellerCommunitySqlMapper.selectSellerCommunityList();
        for (SellerCommunityDto sellerCommunityDto : sellerCommunityDtoList) {
            Map<String,Object> map = new HashMap<>();
            map.put("sellerCommunityDto", sellerCommunityDto);
            map.put("selectTotalCommentReplyCount",sellerCommunitySqlMapper.selectTotalCommentCount(sellerCommunityDto.getSellerCommunityNumber())+sellerCommunitySqlMapper.selectTotalReplyCount(sellerCommunityDto.getSellerCommunityNumber()));
            map.put("selectSellerCommunityLikeCount",sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityDto.getSellerCommunityNumber()));
            map.put("sellerName",sellerCommunitySqlMapper.selectSeller(sellerCommunityDto.getSellerNumber()));
            result.add(map);
        }
        return result;
    }

    public Map<String,Object> selectSellerCommunityById(int sellerCommunityNumber){
        Map<String,Object> map = new HashMap<>();
        map.put("sellerName",sellerCommunitySqlMapper.selectSeller(sellerCommunityNumber));
        map.put("selectSellerCommunityById",sellerCommunitySqlMapper.selectSellerCommunityById(sellerCommunityNumber));
        map.put("selectImageListById",sellerCommunitySqlMapper.selectImageListById(sellerCommunityNumber));
        map.put("selectSellerCommunityLikeCount",sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityNumber));

        List<Map<String,Object>> sellerCommunityCommentDtoList = sellerCommunitySqlMapper.selectSellerCommunityComment(sellerCommunityNumber);
        List<Map<String,Object>> result = new ArrayList<>();
        for(Map<String,Object> sellerCommunityCommentDto: sellerCommunityCommentDtoList){
            Map<String,Object> smallMap = new HashMap<>();
            smallMap.put("selectSellerCommunityReply",sellerCommunitySqlMapper.selectSellerCommunityReply((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber")));
            smallMap.put("sellerCommunityCommentDto",sellerCommunityCommentDto);
            result.add(smallMap);
        }
        map.put("selectSellerCommunityComment",result);

        return map;
    }

    public List<SellerCommunityImageDetailDto> selectImageListById(int sellerCommunityNumber){
        return sellerCommunitySqlMapper.selectImageListById(sellerCommunityNumber);
    }

    //댓글삽입
    public void insertSellerCommunityComment(SellerCommunityCommentDto sellerCommunityCommentDto){
        sellerCommunitySqlMapper.insertSellerCommunityComment(sellerCommunityCommentDto);
    }
    //댓글 가져오기
    public List<Map<String,Object>> selectSellerCommunityComment(int sellerCommunityNumber){
       return sellerCommunitySqlMapper.selectSellerCommunityComment(sellerCommunityNumber);
    }


    //대댓글 삽입
    public void insertSellerCommunityReply(SellerCommunityReplyDto sellerCommunityReplyDto){
        sellerCommunitySqlMapper.insertSellerCommunityReply(sellerCommunityReplyDto);
    }
    //대댓글 가져오기
    public List<Map<String,Object>> selectSellerCommunityReply(int sellerCommunityCommentNumber){
        return  sellerCommunitySqlMapper.selectSellerCommunityReply(sellerCommunityCommentNumber);
    }

    //게시글에 대한 좋아요
    public void insertSellerCommunityLike(SellerCommunityLikeDto sellerCommunityLikeDto){
        sellerCommunitySqlMapper.insertSellerCommunityLike(sellerCommunityLikeDto);
    }
    public int selectSellerCommunityLikeCount(int sellerCommunityNumber){
      return  sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityNumber);
    }
    public void deleteSellerCommunityLike(SellerCommunityLikeDto sellerCommunityLikeDto){
        sellerCommunitySqlMapper.deleteSellerCommunityLike(sellerCommunityLikeDto);
    }
    public int checkIfSellerCommunityLikeExists(SellerCommunityLikeDto sellerCommunityLikeDto){
        return sellerCommunitySqlMapper.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);
    }
}
