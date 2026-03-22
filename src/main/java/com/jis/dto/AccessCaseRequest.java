package com.jis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessCaseRequest {

    private Integer userId;
    private String cin;
}