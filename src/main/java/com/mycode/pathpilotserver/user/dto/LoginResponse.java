package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.system.security.UserRole;
import lombok.Builder;

@Builder
public record LoginResponse(Long id, String email, String token, String username, String name, UserRole userRole) {
}