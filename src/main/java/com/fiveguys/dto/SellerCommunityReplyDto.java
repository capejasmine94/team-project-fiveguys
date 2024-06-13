package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerCommunityReplyDto {
    private int sellerCommunityReplyNumber;
    private int sellerCommunityCommentNumber;
    private int sellerNumber;
    private String sellerCommunityReplyContent;
    private Date sellerCommunityReplyCreatedAt;
}
