package com.fiveguys.master.mapper;

import java.util.List;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventSqlMapper {

    List<EventBoardDto> selectEventList();

    void insertEventProcess(EventBoardDto eventBoardDto);

    void insertDetailImageProcess(EventDetailImageDto eventDetailImageDto);

    int selectRuningEvent();

    EventBoardDto eventBoardDto(int eventNumber);

    List<EventDetailImageDto> eventBoardDetail(int eventNumber);

    void insertEventComment(EventCommentDto eventCommentDto);

    List<EventCommentDto> selectEventBoardComet(int eventNumber);

    CustomerDto selectCustomerDto(int customerNumber);

    int selectEventBoardLikeCheck(EventLikeDto eventLikeDto);

    void deleteEventLike(EventLikeDto eventLikeDto);

    void insertEventLike(EventLikeDto eventLikeDto);

    int selectEventBoardLikeCount(int eventNumber);

    void updateEventBoardVisitCount(int eventNumber);

    List<EventBoardDto> selectEndEventList();

    int selectEndEvent();

    void updateEventCommentMasterReply(EventCommentDto eventCommentDto);

    void insertWinnerProcess(WinnerDto winnerDto);

    void deleteEventProcess(int eventNumber);

    void updateEventProcess(EventBoardDto eventBoardDto);

    void deleteEventDetailImage(int eventNumber);

    List<WinnerDto> selectWinnerList();

    int selectWinnerEventCount();

    WinnerDto selectWinnerDto(int winnerNumber);

    void updateWinnerProcess(WinnerDto winnerDto);

    void deleteWinner(int winnerNumber);

    int selectEventBoardNumerByEventCommentNumber(int eventCommentNumber);

    List<EventBoardDto> selectEventBoardLimit();


}
