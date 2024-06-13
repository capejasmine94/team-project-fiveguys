package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerCommunityLikeDto {
    private int sellerCommunityLike;
    private int sellerCommunityNumber;
    private String sellerCommunityName;
    private int sellerNumber;
    private Date sellerCommunityLikeCreatedAt;
}
