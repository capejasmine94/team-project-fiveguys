package com.fiveguys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDetailImageDto {
    private int eventDetailNumber;
    private int eventNumber;
    private String eventDetailImage;

}
