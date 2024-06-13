package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;
@Data
public class WinnerDto {
    private int winnerNumber;
    private int masterNumber;
    private String winnerTitle;
    private String winnerContent;
    private Date winnerCreatedAt;

}
