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
}
