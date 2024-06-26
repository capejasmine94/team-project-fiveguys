package com.fiveguys.dto;

import lombok.Data;

@Data
public class CustomerOrderMenuDto {
    private int customerNumber;
    private int customerOrderNumber;
    private int menuNumber;
    private int menuOptionNumber;
    private int orderMenuQuantity;
}
