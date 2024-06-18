package com.fiveguys.master.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fiveguys.dto.CustomerDto;
import com.fiveguys.dto.EventBoardDto;
import com.fiveguys.dto.EventCommentDto;
import com.fiveguys.dto.EventDetailImageDto;
import com.fiveguys.dto.EventLikeDto;
import com.fiveguys.dto.MasterDto;
import com.fiveguys.dto.WinnerDto;
import com.fiveguys.master.service.EventService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @RequestMapping("eventlistPage")
    public String listPage(Model model, HttpSession session){
        //진행중 이벤트
        List<EventBoardDto> eventDtoList = eventService.selectEventList();
        int selectRunningEvent = eventService.selectRunningEvent();
        model.addAttribute("eventDtoList", eventDtoList);
        model.addAttribute("selectRunningEvent",selectRunningEvent);
        
        // 종료된 이벤트
        List<EventBoardDto> eventEndDtoList = eventService.selectEndEventList();
        int selectEndEvent = eventService.selectEndEvent();
        model.addAttribute("eventEndDtoList", eventEndDtoList);
        model.addAttribute("selectEndEvent",selectEndEvent);
        MasterDto masterDto = (MasterDto) session.getAttribute("masterDto");

        //이벤트 당첨자
        List<WinnerDto> eventWinnerList = eventService.selectWinnerList();
        int selectWinnerEventCount = eventService.selectWinnerEventCount();
        model.addAttribute("eventWinnerList", eventWinnerList);
        model.addAttribute("selectWinnerEventCount",selectWinnerEventCount);

        if(masterDto != null){
            return "master/eventlistPage";
        }

        return  "customer/eventlistPage";
    }

    @RequestMapping("endEventlistPage")
    public String endEventlistPage(Model model, HttpSession session){
        List<EventBoardDto> eventEndDtoList = eventService.selectEndEventList();
        int selectEndEvent = eventService.selectEndEvent();
        model.addAttribute("eventEndDtoList", eventEndDtoList);
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

        List<EventDetailImageDto> eventDetailImageList = detailImageReName(uploadFiles);
        
        eventService.insertEventProcess(eventBoardDto,eventDetailImageList);

        return "redirect:./eventlistPage";
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
    //게시글 상세 내용
    @RequestMapping("eventDetailPage")
    public String detailPage(@RequestParam("eventNumber") int eventNumber,Model model, HttpSession session){
        //조회수 증가
        eventService.updateEventBoardVisitCount(eventNumber);
        
        //게시글 정보
        Map<String,Object> eventBoardDtoAndDetail =  eventService.eventBoardDtoAndDetail(eventNumber);
        model.addAttribute("eventBoardDtoAndDetail",eventBoardDtoAndDetail);
        List<Map<String,Object>> eventBoardCommentList = eventService.selectEventBoardComet(eventNumber);

        int eventBoardLikeCount = eventService.selectEventBoardLikeCount(eventNumber);
        model.addAttribute("eventBoardLikeCount",eventBoardLikeCount);
        

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
    
    // 댓글 기능
    @RequestMapping("insertEventComment")
    public String insertEventComment(HttpSession session,EventCommentDto eventCommentDto){
        int eventBoardNumber = eventCommentDto.getEventNumber();
        CustomerDto customerDto =  (CustomerDto) session.getAttribute("customerDto");
        eventCommentDto.setCustomerNumber(customerDto.getCustomerNumber());
        eventService.insertEventComment(eventCommentDto);
        return  "redirect:./eventDetailPage?eventNumber="+eventBoardNumber;
    }

    //게시글 좋아요 기능
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


    @RequestMapping("updateMasterReply")
    public String updateMasterReply(EventCommentDto eventCommentDto, @RequestParam("eventNumber") int eventNumber) {
        eventService.updateEventCommentMasterReply(eventCommentDto);
        return "redirect:./eventDetailPage?eventNumber="+eventNumber;

    }
    
    @RequestMapping("winnerInsertPage")
    public String winnerInsertPage(){
        return "master/winnerInsertPage";
    }

    @RequestMapping("insertWinnerProcess")
    public String insertWinnerProcess(WinnerDto winnerDto,HttpSession session){
        MasterDto masterDto = (MasterDto) session.getAttribute("masterDto");
        winnerDto.setMasterNumber(masterDto.getMasterNumber());
        eventService.insertWinnerProcess(winnerDto);
        return "redirect:./eventlistPage";
    }

    @RequestMapping("deleteEventProcess")
    public String deleteEventProcess(@RequestParam("eventNumber")int eventNumber){
        eventService.deleteEventProcess(eventNumber);
        return "redirect:./eventlistPage";
    }

    @RequestMapping("updateEventPage")
    public String updateEventPage(@RequestParam("eventNumber")int eventNumber, Model model){
        Map<String,Object> eventBoardDtoAndDetail =  eventService.eventBoardDtoAndDetail(eventNumber);
        model.addAttribute("eventBoardDtoAndDetail",eventBoardDtoAndDetail);
        
        return "master/updateEventPage";
    }
    @RequestMapping("updateEventProcess")
    public String updateEventProcess(EventBoardDto eventBoardDto, @RequestParam("uploadFile")MultipartFile uploadFile , @RequestParam("uploadFiles")MultipartFile[] uploadFiles){
        String mainImage = mainImageRemake(uploadFile);
        eventBoardDto.setEventMainImage(mainImage);

        List<EventDetailImageDto> eventDetailImageList = detailImageReName(uploadFiles);
        
        eventService.updateEventProcess(eventBoardDto,eventDetailImageList);

        return "redirect:./eventDetailPage?eventNumber="+eventBoardDto.getEventNumber();
    }

    @RequestMapping("updateWinnerPage")
    public String updateWinnerPage(@RequestParam("winnerNumber")int winnerNumber, Model model){
        WinnerDto winnerDto = eventService.selectWinnerDto(winnerNumber);
        model.addAttribute("winnerDto", winnerDto);
        
        return "master/updateWinnerPage";
    }

    @RequestMapping("updateWinnerProcess")
    public String updateWinnerProcess(WinnerDto winnerDto){
        eventService.updateWinnerProcess(winnerDto);
        return"redirect:./eventlistPage";
    }

    @RequestMapping("deleteWinnerProcess")
    public String deleteWinnerProcess(@RequestParam("winnerNumber")int winnerNumber){
        eventService.deleteWinner(winnerNumber);
        return"redirect:./eventlistPage";
    }


    
}