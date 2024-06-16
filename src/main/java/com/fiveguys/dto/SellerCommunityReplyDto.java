package com.fiveguys.dto;

import lombok.Data;

@Data
public class SellerCommunityReplyDto {
    private int sellerCommunityReplyNumber;
    private int sellerCommunityCommentNumber;
    private int sellerNumber;
    private String sellerCommunityReplyContent;
    private String sellerCommunityReplyCreatedAt;
}
