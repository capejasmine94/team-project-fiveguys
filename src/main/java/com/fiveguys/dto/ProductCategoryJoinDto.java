package com.fiveguys.dto;

import lombok.Data;

@Data
public class ProductCategoryJoinDto {
    private int productNumber;
    private String productCategoryName;
    private String productName;
    private int productPrice;

}
