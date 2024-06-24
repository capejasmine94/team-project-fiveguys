package com.fiveguys.dto;

import lombok.Data;

@Data
public class ProductOptionDto {
    private int productOptionNumber;
    private int optionValueNumber;
    private int productNumber;
    private String optionName;
}
