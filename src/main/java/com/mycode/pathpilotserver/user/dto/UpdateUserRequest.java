package com.mycode.pathpilotserver.user.dto;

import lombok.Builder;

@Builder
public record UpdateUserRequest(String email,  String firstName,
                                String lastName, String phone ,
                                String city , String street ,
                                String country , String streetNumber,
                                String postalCode
) {

}
