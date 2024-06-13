package com.fiveguys.dto;

import lombok.Data;

@Data
public class SellerCommunityDto {
    private int sellerCommunityNumber;
    private int sellerNumber;
    private String sellerCommunityImage;
    private String sellerCommunityTitle;
    private String sellerCommunityContent;
    private int sellerCommunityVisitCount;
    private String sellerCommunityCreatedAt;
}
