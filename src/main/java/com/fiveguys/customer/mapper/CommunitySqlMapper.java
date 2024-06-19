package com.fiveguys.customer.mapper;

import com.fiveguys.dto.CommunityCommentDto;
import com.fiveguys.dto.CommunityDto;
import com.fiveguys.dto.CommunityLikeDto;
import com.fiveguys.dto.CustomerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    //댓글
    public void insertCommunityComment(CommunityCommentDto communityCommentDto);
    public List<CommunityCommentDto> selectCommunityCommentList(int communityNumber);
    public int selectCountCommentNumber(int commentNumber);

    //좋아요
    public void insertCommunityLike(CommunityLikeDto communityLikeDto);
    public void deleteLikeNumber(int LikeNumber);
    public CommunityLikeDto selectCommunityLike(CommunityLikeDto communityLikeDto);
    public int selectCountCommunityLike(int communityNumber);



}
