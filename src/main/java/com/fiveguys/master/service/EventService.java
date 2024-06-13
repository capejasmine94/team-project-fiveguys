package com.fiveguys.master.service;

import java.util.List;

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
}
