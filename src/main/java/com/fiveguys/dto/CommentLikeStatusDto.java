package com.fiveguys.dto;

import lombok.Data;

@Data
public class CommentLikeStatusDto {

    private int commentLikeNumber;
    private int customerNumber;
    private int commentNumber;
    private String commentLikeStatus;

}

