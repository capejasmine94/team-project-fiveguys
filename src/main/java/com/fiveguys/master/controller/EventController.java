package com.fiveguys.master.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fiveguys.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fiveguys.master.service.EventService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @RequestMapping("eventlistPage")
    public String listPage(Model model, HttpSession session){
        List<EventBoardDto> eventDtoList = eventService.selectEventList();
        int selectRunningEvent = eventService.selectRunningEvent();
        model.addAttribute("eventDtoList", eventDtoList);
        model.addAttribute("selectRunningEvent",selectRunningEvent);
        MasterDto masterDto = (MasterDto) session.getAttribute("masterDto");

        if(masterDto != null){
            return "master/eventlistPage";
        }

        return  "customer/eventlistPage";
    }

    @RequestMapping("endEventlistPage")
    public String endEventlistPage(Model model, HttpSession session){
        List<EventBoardDto> eventDtoList = eventService.selectEndEventList();
        int selectRunningEvent = eventService.selectRunningEvent();
        int selectEndEvent = eventService.selectEndEvent();
        model.addAttribute("eventDtoList", eventDtoList);
        model.addAttribute("selectRunningEvent",selectRunningEvent);
        model.addAttribute("selectEndEvent",selectEndEvent);
        MasterDto masterDto = (MasterDto) session.getAttribute("masterDto");

        if(masterDto != null){
            return "master/endEventlistPage";
        }

        return  "customer/EndeventlistPage";
    }

    @RequestMapping("eventInsertPage")
    public String eventInsertPage(){

        return "master/eventInsertPage";
    }

    @RequestMapping("insertEventProcess")
    public String insertEventProcess(HttpSession session,EventBoardDto eventBoardDto, @RequestParam("uploadFile")MultipartFile uploadFile , @RequestParam("uploadFiles")MultipartFile[] uploadFiles){
        String mainImage = mainImageRemake(uploadFile);
        eventBoardDto.setEventMainImage(mainImage);

        List<EventDetailImageDto> eventDetailImageList = new ArrayList<>();
        //파일 처리 시작
        if(uploadFiles != null){
            for(MultipartFile multipartFile : uploadFiles){
                if(multipartFile.isEmpty()){
                    continue;
                }

                String rootPath = "C:/fiveguys_image/";

                //날짜별 폴더 생성
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
                String todaypath = sdf.format(new Date());

                File todayFolderForCreate = new File(rootPath + todaypath);

                if(!todayFolderForCreate.exists()){
                    todayFolderForCreate.mkdirs();
                }

                // 파일명 충돌 회피 - 랜덤, 시간조합
                String originalFilename = multipartFile.getOriginalFilename();

                String uuid = UUID.randomUUID().toString();
                long currentTime = System.currentTimeMillis();

                String filename = uuid + "_" + currentTime;

                //확장자명 추출
                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

                filename += ext;

                try {
                    multipartFile.transferTo(new File(rootPath + todaypath + filename));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //DB 작업용 Dto 생성
                EventDetailImageDto eventDetailImageDto = new EventDetailImageDto();
                eventDetailImageDto.setEventDetailImage(todaypath + filename);
                eventDetailImageList.add(eventDetailImageDto);

            }
        }
        //파일 처리 끝
        eventService.insertEventProcess(eventBoardDto,eventDetailImageList);

        return "redirect:./eventlistPage";
    }

    public String mainImageRemake(MultipartFile pp_mainImgLink){

        String rootPath = "C:/fiveguys_image/";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String todaypath = sdf.format(new Date());

        File todayFolderForCreate = new File(rootPath + todaypath);

        if(!todayFolderForCreate.exists()){
            todayFolderForCreate.mkdirs();
        }

        String originalFilename = pp_mainImgLink.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        long currentTime = System.currentTimeMillis();

        String filename = uuid + "_" + currentTime;

        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

        filename += ext;

        try {
            pp_mainImgLink.transferTo(new File(rootPath + todaypath + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String reLocation = todaypath + filename;
        return reLocation;
    }

    @RequestMapping("eventDetailPage")
    public String detailPage(@RequestParam("eventNumber") int eventNumber,Model model, HttpSession session){
        Map<String,Object> eventBoardDtoAndDetail =  eventService.eventBoardDtoAndDetail(eventNumber);
        model.addAttribute("eventBoardDtoAndDetail",eventBoardDtoAndDetail);
        List<Map<String,Object>> eventBoardCommentList = eventService.selectEventBoardComet(eventNumber);

        int eventBoardLikeCount = eventService.selectEventBoardLikeCount(eventNumber);
        model.addAttribute("eventBoardLikeCount",eventBoardLikeCount);
        eventService.updateEventBoardVisitCount(eventNumber);

        MasterDto masterDto = (MasterDto) session.getAttribute("masterDto");
        model.addAttribute("eventBoardCommentList",eventBoardCommentList);

        if(masterDto != null){
            return "master/eventDetailPage";
        }
        int selectLikeCheck =0;
        CustomerDto customerDto = (CustomerDto) session.getAttribute("customerDto");

        if(customerDto != null) {
            EventLikeDto eventLikeDto = new EventLikeDto();
            eventLikeDto.setEventNumber(eventNumber);
            eventLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
            selectLikeCheck = eventService.selectEventBoardLikeCheck(eventLikeDto);
            model.addAttribute("selectLikeCheck", selectLikeCheck);

        }
        return  "customer/eventDetailPage";
    }

    @RequestMapping("insertEventComment")
    public String insertEventComment(HttpSession session,EventCommentDto eventCommentDto){
        int eventBoardNumber = eventCommentDto.getEventNumber();
        CustomerDto customerDto =  (CustomerDto) session.getAttribute("customerDto");
        eventCommentDto.setCustomerNumber(customerDto.getCustomerNumber());
        eventService.insertEventComment(eventCommentDto);
        return  "redirect:./eventDetailPage?eventNumber="+eventBoardNumber;
    }

    @RequestMapping("insertDeleteLikeProcess")
    public String insertDeleteLikeProcess(HttpSession session,@RequestParam("eventNumber")int eventNumber){

        EventLikeDto eventLikeDto = new EventLikeDto();
        CustomerDto customerDto = (CustomerDto)session.getAttribute("customerDto");
        if(customerDto == null){
            return "redirect:/login/customerLogin";
        }
        eventLikeDto.setEventNumber(eventNumber);
        eventLikeDto.setCustomerNumber(customerDto.getCustomerNumber());
        int selectLikeCheck = eventService.selectEventBoardLikeCheck(eventLikeDto);
        if(selectLikeCheck == 1 ){
            eventService.deleteEventLike(eventLikeDto);
        }else{
            eventService.insertEventLike(eventLikeDto);
        }

        return "redirect:./eventDetailPage?eventNumber="+eventNumber;
    }






    
}