package com.mycode.pathpilotserver.user.dto;

import lombok.Builder;

@Builder
public record ResetPasswordRequest(String email, String password, String code) {
}
