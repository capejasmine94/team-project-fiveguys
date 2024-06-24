package com.fiveguys.dto;

import lombok.Data;

@Data
public class ProductDto {
    private int productNumber;
    private int productCategoryNumber;
    private String productName;
    private int productPrice;
    private String productInformation;
    private String productImage;
}
