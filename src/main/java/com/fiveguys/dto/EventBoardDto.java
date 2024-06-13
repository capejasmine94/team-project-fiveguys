package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventBoardDto {
    private int eventNumber;
    private String eventMainImage;
    private String eventTitle;
    private Date eventStartDay;
    private Date eventEndDay;
    private Date eventCreatedAt;
    private int eventVisitCount;

}
