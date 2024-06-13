package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventCommentDto {
    private int eventCommentNumber;
    private int eventNumber;
    private int customerNumber;
    private String eventCommentContent;
    private String masterReply;
    private Date eventCommentCreatedAt;
    private Date masterReplyCreatedAt;
}
