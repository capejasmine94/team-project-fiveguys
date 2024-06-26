package com.fiveguys.dto;

import lombok.Data;

@Data
public class CustomerMenuOptionDto {
    private int menuOptionNumber;
    private int menuNumber;
    private String menuOptionName;
    private String menuOptionContent;
    private int menuOptionPrice;
}
