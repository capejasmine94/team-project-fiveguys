package com.fiveguys.seller.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SellerCommunitySqlMapper {

    public SellerDto selectSeller(int sellerNumber);

    public void insertSellerCommunityWrite(SellerCommunityDto sellerCommunityDto);
    public void insertSellerCommunityImageDetail(SellerCommunityImageDetailDto sellerCommunityImageDetailDto);

    public List<SellerCommunityDto> selectSellerCommunityList();
    public SellerCommunityDto selectSellerCommunityById(int sellerCommunityNumber);
    public List<SellerCommunityImageDetailDto> selectImageListById(int sellerCommunityNumber);

    //댓글삽입
    public void insertSellerCommunityComment(SellerCommunityCommentDto sellerCommunityCommentDto);
    //댓글 가져오기
    public List<Map<String,Object>> selectSellerCommunityComment(int sellerCommunityNumber);

    //대댓글 삽입
    public void insertSellerCommunityReply(SellerCommunityReplyDto sellerCommunityReplyDto);
    //대댓글 가져오기
    public List<Map<String,Object>> selectSellerCommunityReply(int sellerCommunityCommentNumber);

    //댓글 대댓글 카운트
    public Integer selectTotalCommentCount(int sellerCommunityNumber);
    public Integer selectTotalReplyCount(int sellerCommunityNumber);

    //게시글에 대한 좋아요
    public void insertSellerCommunityLike(SellerCommunityLikeDto sellerCommunityLikeDto);
    public Integer selectSellerCommunityLikeCount(int sellerCommunityNumber);
    public void deleteSellerCommunityLike(SellerCommunityLikeDto sellerCommunityLikeDto);
    public Integer checkIfSellerCommunityLikeExists(SellerCommunityLikeDto sellerCommunityLikeDto);
}
