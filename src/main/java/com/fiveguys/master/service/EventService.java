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

    public int selectEventBoardLikeCount(int eventNumber) {
        return eventSqlMapper.selectEventBoardLikeCount(eventNumber);
    }

    public void updateEventBoardVisitCount(int eventNumber) {
        eventSqlMapper.updateEventBoardVisitCount(eventNumber);
    }

    public List<EventBoardDto> selectEndEventList() {
        return eventSqlMapper.selectEndEventList();
    }

    public int selectEndEvent() {
        return eventSqlMapper.selectEndEvent();
    }

    public void updateEventCommentMasterReply(EventCommentDto eventCommentDto) {
        eventSqlMapper.updateEventCommentMasterReply(eventCommentDto);
    }

    public void insertWinnerProcess(WinnerDto winnerDto) {
        eventSqlMapper.insertWinnerProcess(winnerDto);
    }

    public void deleteEventProcess(int eventNumber) {
        eventSqlMapper.deleteEventProcess(eventNumber);
    }

    public void updateEventProcess(EventBoardDto eventBoardDto, List<EventDetailImageDto> eventDetailImageList) {
        eventSqlMapper.updateEventProcess(eventBoardDto);
        eventSqlMapper.deleteEventDetailImage(eventBoardDto.getEventNumber());
        for(EventDetailImageDto eventDetailImageDto : eventDetailImageList){
            eventDetailImageDto.setEventNumber(eventBoardDto.getEventNumber());
            eventSqlMapper.insertDetailImageProcess(eventDetailImageDto);
        }
    }

    public List<WinnerDto> selectWinnerList() {
        return eventSqlMapper.selectWinnerList();
    }

    public int selectWinnerEventCount() {
        return eventSqlMapper.selectWinnerEventCount();
    }

    public WinnerDto selectWinnerDto(int winnerNumber) {
        return eventSqlMapper.selectWinnerDto(winnerNumber);
    }

    public void updateWinnerProcess(WinnerDto winnerDto) {
        eventSqlMapper.updateWinnerProcess(winnerDto);
    }

    public void deleteWinner(int winnerNumber) {
        eventSqlMapper.deleteWinner(winnerNumber);
    }

    public List<Map<String, Object>> selectRunningEventInfoList(CustomerDto customerDto) {
        List<Map<String, Object>> eventBoardInfoList = new ArrayList<>();
        List<EventBoardDto> eventBoardDtoList = eventSqlMapper.selectEventList();

        for(EventBoardDto eventBoardDto : eventBoardDtoList){
            Map<String,Object> map = new HashMap<>();
            int eventBoardLikeCount = eventSqlMapper.selectEventBoardLikeCount(eventBoardDto.getEventNumber());
            int eventLikeCustomerCheck = 0;
            map.put("eventLikeCustomerCheck",eventLikeCustomerCheck);
            if(customerDto != null){
                EventLikeDto eventLikeDto = new EventLikeDto();
                eventLikeDto.setEventNumber(eventBoardDto.getEventNumber());
                eventLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
                eventLikeCustomerCheck = eventSqlMapper.selectEventBoardLikeCheck(eventLikeDto);
                map.put("eventLikeCustomerCheck",eventLikeCustomerCheck);
            }

            map.put("eventBoardLikeCount",eventBoardLikeCount);

            map.put("eventBoardDto",eventBoardDto);
            eventBoardInfoList.add(map);
        }


        return eventBoardInfoList;

    }

    public List<Map<String, Object>> selectEndEventInfoList(CustomerDto customerDto) {
        List<Map<String, Object>> eventBoardInfoList = new ArrayList<>();
        List<EventBoardDto> eventBoardDtoList = eventSqlMapper.selectEndEventList();

        for(EventBoardDto eventBoardDto : eventBoardDtoList){
            Map<String,Object> map = new HashMap<>();
            int eventBoardLikeCount = eventSqlMapper.selectEventBoardLikeCount(eventBoardDto.getEventNumber());
            int eventLikeCustomerCheck = 0;
            map.put("eventLikeCustomerCheck",eventLikeCustomerCheck);
            if(customerDto != null){
                EventLikeDto eventLikeDto = new EventLikeDto();
                eventLikeDto.setEventNumber(eventBoardDto.getEventNumber());
                eventLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
                eventLikeCustomerCheck = eventSqlMapper.selectEventBoardLikeCheck(eventLikeDto);
                map.put("eventLikeCustomerCheck",eventLikeCustomerCheck);
            }

            map.put("eventBoardLikeCount",eventBoardLikeCount);

            map.put("eventBoardDto",eventBoardDto);
            eventBoardInfoList.add(map);
        }


        return eventBoardInfoList;
    }
}
