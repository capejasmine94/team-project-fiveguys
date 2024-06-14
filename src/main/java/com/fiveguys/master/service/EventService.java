package com.fiveguys.master.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiveguys.dto.*;
import com.fiveguys.master.mapper.EventSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventSqlMapper eventSqlMapper;

    public List<EventBoardDto> selectEventList() {

        return eventSqlMapper.selectEventList();
    }

    public void insertEventProcess(EventBoardDto eventBoardDto, List<EventDetailImageDto> eventDetailImageList) {
        eventSqlMapper.insertEventProcess(eventBoardDto);
        for(EventDetailImageDto eventDetailImageDto : eventDetailImageList){
            eventDetailImageDto.setEventNumber(eventBoardDto.getEventNumber());
            eventSqlMapper.insertDetailImageProcess(eventDetailImageDto);
        }
    }

    public int selectRunningEvent() {
        return eventSqlMapper.selectRuningEvent();
    }


    public Map<String, Object> eventBoardDtoAndDetail(int eventNumber) {
        Map<String,Object> map = new HashMap<>();
        EventBoardDto eventBoardDto =  eventSqlMapper.eventBoardDto(eventNumber);
        List<EventDetailImageDto> eventDetailImageDtoList =  eventSqlMapper.eventBoardDetail(eventNumber);
        map.put("eventBoardDto",eventBoardDto);
        map.put("eventDetailImageDtoList",eventDetailImageDtoList);

        return map;
    }

    public void insertEventComment(EventCommentDto eventCommentDto) {
        eventSqlMapper.insertEventComment(eventCommentDto);
    }



    public List<Map<String, Object>> selectEventBoardComet(int eventNumber) {
        List<Map<String,Object>> selectEventBoardCometInfoList = new ArrayList<>();
        List<EventCommentDto> selectEventCommentList = eventSqlMapper.selectEventBoardComet(eventNumber);
        for(EventCommentDto eventCommentDto: selectEventCommentList){
            Map<String,Object> map = new HashMap<>();
            CustomerDto customerDto = eventSqlMapper.selectCustomerDto(eventCommentDto.getCustomerNumber());
            map.put("customerDto",customerDto);
            map.put("eventCommentDto",eventCommentDto);

            selectEventBoardCometInfoList.add(map);
        }
        return selectEventBoardCometInfoList;
    }

    public int selectEventBoardLikeCheck(EventLikeDto eventLikeDto) {
         return eventSqlMapper.selectEventBoardLikeCheck(eventLikeDto);
    }

    public void deleteEventLike(EventLikeDto eventLikeDto) {
        eventSqlMapper.deleteEventLike(eventLikeDto);
    }

    public void insertEventLike(EventLikeDto eventLikeDto) {
        eventSqlMapper.insertEventLike(eventLikeDto);
    }
}
