package com.fiveguys.master.mapper;

import java.util.List;

import com.fiveguys.dto.EventDetailImageDto;
import org.apache.ibatis.annotations.Mapper;

import com.fiveguys.dto.EventBoardDto;

@Mapper
public interface EventSqlMapper {

    List<EventBoardDto> selectEventList();

    void insertEventProcess(EventBoardDto eventBoardDto);

    void insertDetailImageProcess(EventDetailImageDto eventDetailImageDto);
}
