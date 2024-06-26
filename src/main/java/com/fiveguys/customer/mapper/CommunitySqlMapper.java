package com.fiveguys.customer.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommunitySqlMapper {

    public void insertCommunityWrite(CommunityDto communityDto);
    public List<CommunityDto> selectCommunityList();

    public CustomerDto selectCustomerNumber(int customerNumber);
    public CommunityDto selectCommunityNumber(int communityNumber);

    // 조회수
    public void updateVisitCount(int communityNumber);

    //글 삭제,수정
    public void deleteCommunityPage(int communityNumber);
    public void updateCommunityPage(CommunityDto communityDto);

    //이미지
    public void communityDetailImageDtoList(CommunityDetailImageDto communityDetailImageDto);
    public List<CommunityDetailImageDto> selectCommunityDatailImageDtoList(int communityId);

    //댓글
    public void insertCommunityComment(CommunityCommentDto communityCommentDto);
    public List<CommunityCommentDto> selectCommunityCommentList(@Param("communityNumber") int communityNumber, @Param("customerNumber") int customerNumber);
    public int selectCountCommentNumber(int commentNumber);
    public CommentLikeStatusDto checkSelectCommunityCommentExists(CommentLikeStatusDto commentLikeStatusDto);

    //좋아요
    public void insertCommunityLike(CommunityLikeDto communityLikeDto);
    public void deleteLikeNumber(int LikeNumber);
    public CommunityLikeDto selectCommunityLike(CommunityLikeDto communityLikeDto);
    public int selectCountCommunityLike(int communityNumber);

    //댓글 좋아요 싫어요
    public void insertCommentLike(CommentLikeStatusDto commentLikeStatusDto);
    public void deleteCommentLikeNumber(int commentLikeNumber);
    public CommentLikeStatusDto selectCommentLike(CommentLikeStatusDto commentLikeStatusDto);
    public int selectCountCommunityCommentLike(int commentNumber);

    //대댓글
    public void insertCommunityCommentReply(CommunityCommentReplyDto communityCommentReplyDto);
    public List<CommunityCommentReplyDto> selectCommunityCommentReplyList(int communityNumber);
    
    //메인 페이지 커뮤니티 게시글 보여주기
    public List<CommunityDto> selectCommunityLimit();




}
