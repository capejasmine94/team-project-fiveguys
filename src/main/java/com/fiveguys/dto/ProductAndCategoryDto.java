package com.fiveguys.dto;

import lombok.Data;

@Data
public class ProductAndCategoryDto {
    private int productNumber;
    private int categoryNumber;
    private String productName;
    private int productPrice;
    private String productInformation;
    private String productImage;
    private String categoryName;
}
