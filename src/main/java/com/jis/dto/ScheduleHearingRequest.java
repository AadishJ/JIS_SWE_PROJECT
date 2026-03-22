package com.jis.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleHearingRequest {

    private String cin;
    private LocalDate hearingDate;
    private String courtSlot;
}