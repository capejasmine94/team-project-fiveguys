package com.fiveguys.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class EventBoardDto {
    private int eventNumber;
    private String eventMainImage;
    private String eventTitle;
    private LocalDate eventStartDay;
    private LocalDate eventEndDay;
    private Date eventCreatedAt;
    private int eventVisitCount;

}
