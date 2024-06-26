package com.fiveguys.dto;

import lombok.Data;

@Data
public class CustomerMenuDto {
    private int menuNumber;
    private String menuCategory;
    private String menuName;
    private int menuPrice;
    private String menuInformation;
    private String menuImage;
}
