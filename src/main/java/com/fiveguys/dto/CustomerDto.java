package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto {

    private int customerNumber;
    private String customerId;
    private String customerPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerGender;
    private Date customerBirth;
    private String customerCreatedAt;
}
