package com.fiveguys.master.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fiveguys.dto.EventDetailImageDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fiveguys.dto.EventBoardDto;
import com.fiveguys.master.service.EventService;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @RequestMapping("eventlistPage")
    public String listPage(Model model){
        List<EventBoardDto> eventDtoList = eventService.selectEventList();
        model.addAttribute("eventDtoList", eventDtoList);
        return "master/eventlistPage";
    }

    @RequestMapping("eventInsertPage")
    public String eventInsertPage(){

        return "master/eventInsertPage";
    }

    @RequestMapping("insertEventProcess")
    public String insertEventProcess(HttpSession session,EventBoardDto eventBoardDto,MultipartFile uploadFile ,MultipartFile[] uploadFiles){

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
        

        return "redirect:./eventListPage";
    }

    public String mainImageRemake(MultipartFile pp_mainImgLink){

        String rootPath = "C:/uploadFiles/";

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



    
}