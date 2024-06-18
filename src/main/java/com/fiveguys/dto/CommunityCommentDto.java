package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommunityCommentDto {

    private int commentNumber;
    private int customerNumber;
    private int communityNumber;
    private String commentText;
    private String commentCreatedAt;

}
