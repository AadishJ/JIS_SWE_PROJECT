package com.jis.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResolvedCaseResponse {

    private LocalDate caseStartDate;
    private String cin;
    private LocalDate judgmentDate;
    private String attendingJudgeName;
    private String judgmentSummary;
}
