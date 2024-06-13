package com.fiveguys.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommunityDto {

    private int communityNumber;
    private int customerNumber;
    private String communityTitle;
    private String communityContent;
    private int communityVisitCount;
    private Date communityCreatedAt;
}
