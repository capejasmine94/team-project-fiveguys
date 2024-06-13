package com.fiveguys.master.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fiveguys.dto.EventBoardDto;

@Mapper
public interface EventSqlMapper {

    List<EventBoardDto> selecteventList();

}
