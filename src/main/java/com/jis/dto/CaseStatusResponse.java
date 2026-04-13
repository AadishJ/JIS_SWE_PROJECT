package com.jis.dto;

import com.jis.entity.Case;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseStatusResponse {

    private String cin;
    private Case.Status status;
}
