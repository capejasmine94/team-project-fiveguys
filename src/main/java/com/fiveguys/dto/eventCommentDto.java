package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventCommentDto {
    private int eventCommentNumber;
    private int eventNumber;
    private int customerNumber;
    private Date eventCommentContent;
    private Date masterReply;
    private Date eventCommentCreatedAt;
    private Date masterReplyCreatedAt;
}
