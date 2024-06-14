package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SellerOrder {

    private int sellerOrderNumber;
    private int sellerNumber;
    private int materialNumber;
    private int sellerOrderQuantity;
    private Date sellerOrderCreatedAt;
    private String sellerOrderStatus;

}
