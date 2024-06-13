package com.fiveguys.master.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fiveguys.dto.EventBoardDto;
import com.fiveguys.master.service.EventService;


@Controller
@RequestMapping("event")
public class EventController {
    @Autowired
    private EventService eventService;
    
    @RequestMapping("listPage")
    public String listPage(Model model){
        List<EventBoardDto> eventDtoList = eventService.selecteventList();
        model.addAttribute("eventDtoList", eventDtoList);
        return "event/listPage";
    }

    
}