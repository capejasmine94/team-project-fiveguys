package com.fiveguys.dto;

import lombok.Data;

@Data
public class CustomerCartDto {
    private int cartNumber;
    private int customerNumber;
    private int sellerNumber;
    private int menuNumber;
    private int menuOptionNumber;
    private int customerOrderNumber;
    private int cartQuantity;
}
