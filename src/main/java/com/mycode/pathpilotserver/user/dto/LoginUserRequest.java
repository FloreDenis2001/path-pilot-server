package com.mycode.pathpilotserver.user.dto;

import lombok.Builder;

@Builder
public record LoginUserRequest(String email, String password) {
}
