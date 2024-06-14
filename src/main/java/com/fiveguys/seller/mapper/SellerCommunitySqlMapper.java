package com.fiveguys.seller.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SellerCommunitySqlMapper {

    public SellerDto selectSeller(int sellerCommunityNumber);

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

    //게시글 조회수 증가
    public void updateSellerCommunityVisitCount(int sellerCommunityNumber);

    //댓글 좋아요 싫어요
    public void insertSellerCommentLikeStatus(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto);
    public SellerCommunityCommentLikeStatusDto selectSellerCommentLikeStatus(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto);
    public void updateSellerCommentLikeStatus(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto);
    public Integer selectSellerCommentLikeCount(int sellerCommunityCommentLikeStatusNumber);
    public Integer selectSellerCommentDisLikeCount(int sellerCommunityCommentLikeStatusNumber);

    //대댓글 좋아요 싫어요
    public void insertSellerReplyLikeStatus(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto);
    public SellerCommunityReplyLikeStatusDto selectSellerReplyLikeStatus(int sellerCommunityReplyNumber);
    public Integer selectSellerReplyLikeCount(int sellerCommunityReplyLikeStatusNumber);
    public Integer selectSellerReplyDisLikeCount(int sellerCommunityReplyLikeStatusNumber);
    public void updateSellerReplyLikeStatus(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto);
}
