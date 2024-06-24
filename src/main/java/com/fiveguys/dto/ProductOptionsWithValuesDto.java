package com.fiveguys.dto;

import lombok.Data;

@Data
public class ProductOptionsWithValuesDto {
    private int productOptionNumber;
    private int optionValueNumber;
    private int productNumber;
    private String optionName;
    private String optionValueName;
    private int additionalPrice;
}
