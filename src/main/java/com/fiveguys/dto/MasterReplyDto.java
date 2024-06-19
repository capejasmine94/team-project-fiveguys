package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MasterReplyDto {
    private int masterReplyNumber;
    private int sellerReviewNumber;
    private String masterReplyContent;
    private Date masterReplyCreatedAt;
}
