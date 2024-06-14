package com.fiveguys.master.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiveguys.dto.EventDetailImageDto;
import com.fiveguys.master.mapper.EventSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiveguys.dto.EventBoardDto;

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
}
