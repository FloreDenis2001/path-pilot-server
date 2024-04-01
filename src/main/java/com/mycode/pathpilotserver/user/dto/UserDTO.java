package com.mycode.pathpilotserver.user.dto;

import lombok.Builder;

@Builder
public record UserDTO(String firstName, String lastName, String phoneNumber, String email, String password, boolean active) {
}