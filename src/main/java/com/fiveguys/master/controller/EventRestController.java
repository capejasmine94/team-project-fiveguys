package com.fiveguys.master.controller;

import com.fiveguys.dto.*;
import com.fiveguys.master.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/event")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @RequestMapping("eventListPage")
    public Map<String,Object> eventListPage(HttpSession session){
        Map<String,Object> map = new HashMap<>();

        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");

        List<Map<String,Object>> runningEventInfoList = eventService.selectRunningEventInfoList(customerDto);
        map.put("runningEventInfoList",runningEventInfoList);


        List<Map<String,Object>> endEventInfoList = eventService.selectEndEventInfoList(customerDto);
        map.put("endEventInfoList",endEventInfoList);


        return map;
    }

    @RequestMapping("eventDetailPage")
    public Map<String,Object> detailPage(@RequestParam("eventNumber") int eventNumber, Model model, HttpSession session){
        Map<String,Object> map = new HashMap<>();

        eventService.updateEventBoardVisitCount(eventNumber);
        //게시글 정보
        Map<String,Object> eventBoardDtoAndDetail =  eventService.eventBoardDtoAndDetail(eventNumber);
        map.put("eventBoardDtoAndDetail",eventBoardDtoAndDetail);
        List<Map<String,Object>> eventBoardCommentList = eventService.selectEventBoardComet(eventNumber);

        int eventBoardLikeCount = eventService.selectEventBoardLikeCount(eventNumber);
        map.put("eventBoardLikeCount",eventBoardLikeCount);

        
        MasterDto masterDto = (MasterDto) session.getAttribute("masterDto");
        map.put("eventBoardCommentList",eventBoardCommentList);

        int selectLikeCheck =0;
        CustomerDto customerDto = (CustomerDto) session.getAttribute("customerDto");
        map.put("loginInfo","loginfail");
        if(customerDto != null) {
            EventLikeDto eventLikeDto = new EventLikeDto();
            eventLikeDto.setEventNumber(eventNumber);
            eventLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
            selectLikeCheck = eventService.selectEventBoardLikeCheck(eventLikeDto);

            map.put("loginInfo","loginSuccess");
        }
        map.put("selectLikeCheck", selectLikeCheck);
        return  map;
    }

    //게시글 좋아요 기능
    @RequestMapping("insertDeleteLikeProcess")
    public Map<String,Object> insertDeleteLikeProcess(HttpSession session,@RequestParam("eventNumber")int eventNumber){

        Map<String,Object> map = new HashMap<>();

        EventLikeDto eventLikeDto = new EventLikeDto();
        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");
        if(customerDto != null){
            map.put("loginSuccess","true");
            eventLikeDto.setEventNumber(eventNumber);
            eventLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
            int selectLikeCheck = eventService.selectEventBoardLikeCheck(eventLikeDto);

            if(selectLikeCheck == 1 ){
                eventService.deleteEventLike(eventLikeDto);
            }else{
                eventService.insertEventLike(eventLikeDto);
            }
        }else {
            map.put("loginSuccess","false");
        }

        return map;
    }

    
}
