package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.system.security.UserRole;

public record RegisterResponse(Long id, String username, String firstName, String lastName, UserRole role, String email, String phone, String token) {
}