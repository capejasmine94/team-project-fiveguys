package com.fiveguys.seller.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SellerCommunitySqlMapper {

    public SellerDto selectSeller(int sellerCommunityNumber);

    public void insertSellerCommunityWrite(SellerCommunityDto sellerCommunityDto);
    public void insertSellerCommunityImageDetail(SellerCommunityImageDetailDto sellerCommunityImageDetailDto);

    public List<SellerCommunityDto> selectSellerCommunityList(SellerCommunityPaginationDto sellerCommunityPaginationDto);
    public SellerCommunityDto selectSellerCommunityById(int sellerCommunityNumber);
    public List<SellerCommunityImageDetailDto> selectImageListById(int sellerCommunityNumber);

    //전체 게시물 개수 가져오기, 검색된 후의 게시물도 포함
    public Integer selectSellerCommunityCount(String searchWord);

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
    //댓글에 달린 대댓글 카운트
    public Integer selectEachSellerCommentReplyCount(int sellerCommunityCommentNumber);

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
    public Integer selectSellerCommentLikeCount(int sellerCommunityCommentNumber);
    public Integer selectSellerCommentDisLikeCount(int sellerCommunityCommentNumber);

    //대댓글 좋아요 싫어요
    public void insertSellerReplyLikeStatus(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto);
    public SellerCommunityReplyLikeStatusDto selectSellerReplyLikeStatus(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto);
    public Integer selectSellerReplyLikeCount(int sellerCommunityReplyNumber);
    public Integer selectSellerReplyDisLikeCount(int sellerCommunityReplyNumber);
    public void updateSellerReplyLikeStatus(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto);
}
