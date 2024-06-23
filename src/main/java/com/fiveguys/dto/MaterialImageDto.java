package com.fiveguys.dto;

import lombok.Data;

@Data
public class MaterialImageDto {

    private int materialImageNumber;
    private int materialNumber;
    private String location;
    private String original_filename;
}
