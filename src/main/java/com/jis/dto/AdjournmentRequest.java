package com.jis.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdjournmentRequest {

    private Integer hearingId;
    private String reason;
    private LocalDate newHearingDate;
}