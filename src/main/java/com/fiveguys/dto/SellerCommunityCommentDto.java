package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerCommunityCommentDto {
    private int sellerCommunityCommentNumber;
    private int sellerCommunityNumber;
    private int sellerNumber;
    private String sellerCommunityCommentContent;
    private Date sellerCommunityCommentCreatedAt;
}
