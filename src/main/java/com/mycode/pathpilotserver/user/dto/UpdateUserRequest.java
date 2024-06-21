package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.user.models.User;
import lombok.Builder;
import lombok.Data;

@Builder
public record UpdateUserRequest(String email,  String firstName,
                                String lastName, String phone ,
                                String city , String street ,
                                String country , String streetNumber,
                                String postalCode
) {

}
