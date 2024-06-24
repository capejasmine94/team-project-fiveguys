package com.fiveguys.dto;

import lombok.Data;

@Data
public class OrderMenuProductDto {
    private int customerOrderNumber;
    private int productNumber;
    private int orderMenuPrice;
    private int orderMenuQuantity;
    private String productName;
    private String productImage;
}
