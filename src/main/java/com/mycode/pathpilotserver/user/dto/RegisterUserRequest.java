package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.address.models.Address;

public record RegisterUserRequest(String username, String email, String firstName, String lastName, String password, String phone,
                                  Address address) {
}
