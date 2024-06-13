package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerReview {

    private int sellerReviewNumber;
    private int sellerOrderNumber;
    private String sellerReviewContent;
    private Date sellerReviewCreatedAt;
    private String masterReply;
    private Date masterReplyCreatedAt;

}
