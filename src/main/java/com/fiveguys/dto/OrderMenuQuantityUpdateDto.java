package com.fiveguys.dto;

import lombok.Data;

@Data
public class OrderMenuQuantityUpdateDto {
    private int customerOrderNumber;
    private int productNumber;
    private int orderMenuPrice;
    private int orderMenuQuantity;
    private int  productPrice;
}
