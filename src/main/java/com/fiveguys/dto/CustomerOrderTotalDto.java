package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerOrderTotalDto {
    private int customerOrderNumber;
    private int customerNumber;
    private int orderMenuQuantity;
    private String menuOptionName;
    private String menuOptionContent;
    private int menuOptionPrice;
    private String menuName;
    private int menuPrice;
    private String menuImage;
    private int customerOrderTotalPrice;
    private Date customerOrderCreatedAt;
}
