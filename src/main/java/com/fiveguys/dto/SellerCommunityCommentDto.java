package com.fiveguys.dto;

import lombok.Data;

@Data
public class SellerCommunityCommentDto {
    private int sellerCommunityCommentNumber;
    private int sellerCommunityNumber;
    private int sellerNumber;
    private String sellerCommunityCommentContent;
    private String sellerCommunityCommentCreatedAt;
}
