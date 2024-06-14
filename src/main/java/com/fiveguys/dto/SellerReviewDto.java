package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerReviewDto {

    private int sellerReviewNumber;
    private int sellerOrderNumber;
    private String sellerReviewContent;
    private Date sellerReviewCreatedAt;
    private String masterReply;
    private Date masterReplyCreatedAt;

}
