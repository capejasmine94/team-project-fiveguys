package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MasterDto {
    private int masterNumber;
    private String masterId;
    private String masterPassword;
    private String masterNickName;
    private Date materCreatedAt;
}
