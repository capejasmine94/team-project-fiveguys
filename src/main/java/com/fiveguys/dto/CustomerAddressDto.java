package com.fiveguys.dto;

import lombok.Data;

@Data
public class CustomerAddressDto {
    private int customerAddressNumber;
    private int customerNumber;
    private String deliveryAddress;
}
