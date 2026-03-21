package com.jis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCaseRequest {

    private String defendantDetails;
    private String crimeType;
    private String arrestInfo;
    private String prosecutorDetails;
    private Integer createdBy; // userId (Registrar)
}