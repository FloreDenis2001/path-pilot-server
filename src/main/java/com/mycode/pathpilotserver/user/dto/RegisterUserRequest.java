package com.mycode.pathpilotserver.user.dto;

public record RegisterUserRequest(String username,String email,String firstName,String lastName,String password,String phone) {
}
