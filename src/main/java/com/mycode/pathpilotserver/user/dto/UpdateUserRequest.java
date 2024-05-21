package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.user.models.User;
import lombok.Builder;

@Builder
public record UpdateUserRequest(String email,User newUser){
}
