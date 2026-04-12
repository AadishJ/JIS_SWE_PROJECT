package com.jis.security;

import com.jis.entity.User;

public record AuthenticatedUser(Integer userId, String name, User.Role role) {
}
