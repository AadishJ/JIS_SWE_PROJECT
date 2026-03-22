package com.jis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCaseRequest {

    private String defendantDetails;
    private String crimeType;
    private String arrestInfo;
    private String prosecutorDetails;
}