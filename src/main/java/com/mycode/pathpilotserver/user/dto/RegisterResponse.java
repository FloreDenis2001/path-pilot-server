package com.mycode.pathpilotserver.user.dto;

public record RegisterResponse(Long id, String firstName, String secondName, String email,String phone, String token) {
}