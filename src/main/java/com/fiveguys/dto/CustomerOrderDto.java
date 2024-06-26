package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerOrderDto {
    private int customerOrderNumber;
    private int customerNumber;
    private int sellerNumber;
    private int customerOrderTotalPrice;
    private Date customerOrderCreatedAt;
}
