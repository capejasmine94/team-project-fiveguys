package com.fiveguys.dto;

import lombok.Data;

@Data
public class OrderMenuDto {
    private int customerOrderNumber;
    private int productNumber;
    private int productOptionNumber;
    private int orderMenuPrice;
    private int orderMenuQuantity;
    private int orderMenuState;
}
