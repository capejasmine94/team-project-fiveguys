package com.fiveguys.master.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fiveguys.dto.*;
import com.fiveguys.master.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        int selectRunningEvent = eventService.selectRunningEvent();
        map.put("selectRunningEvent", selectRunningEvent);
        map.put("runningEventInfoList",runningEventInfoList);


        List<Map<String,Object>> endEventInfoList = eventService.selectEndEventInfoList(customerDto);
        int selectEndEvent = eventService.selectEndEvent();
        map.put("selectEndEvent", selectEndEvent);
        map.put("endEventInfoList",endEventInfoList);

        //이벤트 당첨자
        List<WinnerDto> eventWinnerList = eventService.selectWinnerList();
        int selectWinnerEventCount = eventService.selectWinnerEventCount();
        map.put("eventWinnerList", eventWinnerList);
        map.put("selectWinnerEventCount", selectWinnerEventCount);


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
        map.put("masterDto",masterDto);

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

    // 댓글 기능
    @RequestMapping("insertEventComment")
    public Map<String,Object> insertEventComment(HttpSession session,@RequestBody EventCommentDto eventCommentDto){
        Map<String,Object> map = new HashMap<>();
        CustomerDto customerDto =  (CustomerDto) session.getAttribute("customerDto");
        eventCommentDto.setCustomerNumber(customerDto.getCustomerNumber());
        eventService.insertEventComment(eventCommentDto);
        map.put("pageNumber", eventCommentDto.getEventNumber());

        return  map;
    }
    
    //대댓글
    @RequestMapping("updateMasterReply")
    // 하나의 데이터 객체를 받을 때 잘 작동되는 것 같음
    public Map<String,Object> updateMasterReply(@RequestBody EventCommentDto eventCommentDto) {
        Map<String,Object> map = new HashMap<>();
        eventService.updateEventCommentMasterReply(eventCommentDto);
        map.put("eventNumber", eventCommentDto.getEventNumber());
        return map;

    }
    @RequestMapping("insertEventProcess")
    public Map<String,Object> insertEventProcess(HttpSession session, EventBoardDto eventBoardDto, 
    @RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("uploadFiles") MultipartFile[] uploadFiles){

        Map<String,Object> map = new HashMap<>();
        String mainImage = mainImageRemake(uploadFile);
        eventBoardDto.setEventMainImage(mainImage);
        List<EventDetailImageDto> eventDetailImageList = detailImageReName(uploadFiles);
        
        eventService.insertEventProcess(eventBoardDto,eventDetailImageList);

        map.put("result", "success");

        return map;
    }

    public List<EventDetailImageDto> detailImageReName(MultipartFile[] uploadFiles){
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
        return eventDetailImageList;
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

    @RequestMapping("updateEventPage")
    public Map<String,Object> updateEventPage(@RequestParam("eventNumber") int eventNumber){
              
        return eventService.eventBoardDtoAndDetail(eventNumber);
    }

    @RequestMapping("updateEventProcess")
    public Map<String,Object> updateEventProcess(EventBoardDto eventBoardDto, @RequestParam("uploadFile")MultipartFile uploadFile , @RequestParam("uploadFiles")MultipartFile[] uploadFiles){
        Map<String,Object> map = new HashMap<>();

       
        String mainImage = mainImageRemake(uploadFile);
        eventBoardDto.setEventMainImage(mainImage);

        List<EventDetailImageDto> eventDetailImageList = detailImageReName(uploadFiles);
        
        eventService.updateEventProcess(eventBoardDto,eventDetailImageList);
        map.put("success", "success");

        return map;
    }
    
    @RequestMapping("deleteEventProcess")
    public Map<String,Object> deleteEventProcess(@RequestParam("eventNumber")int eventNumber){
        eventService.deleteEventProcess(eventNumber);
        Map<String,Object> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }
    
}
