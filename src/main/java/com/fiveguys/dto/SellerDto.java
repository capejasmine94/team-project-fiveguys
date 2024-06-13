package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerDto {
    private int sellerNumber;
    private String sellerId;
    private String sellerPassword;
    private String sellerName;
    private String sellerPhoneNumber;
    private String sellerAddress;
    private Date sellerCreatedAt;
}
