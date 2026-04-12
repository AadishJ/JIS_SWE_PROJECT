package com.jis.dto;

import com.jis.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserResponse {

    private final Integer userId;
    private final String name;
    private final User.Role role;
}
