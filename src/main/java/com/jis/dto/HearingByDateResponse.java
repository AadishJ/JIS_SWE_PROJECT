package com.jis.dto;

import java.time.LocalDate;

import com.jis.entity.Case;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HearingByDateResponse {

    private Integer hearingId;
    private String cin;
    private LocalDate hearingDate;
    private String courtSlot;
    private Case.Status caseStatus;
    private String defendantDetails;
    private String crimeType;
}
