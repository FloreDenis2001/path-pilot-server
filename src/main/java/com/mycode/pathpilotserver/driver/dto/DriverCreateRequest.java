package com.mycode.pathpilotserver.driver.dto;

import com.mycode.pathpilotserver.address.dto.AddressDTO;

public record DriverCreateRequest(String username, String email, String firstName, String lastName, String password,
                                  String phone, String licenseNumber, String companyRegistrationNumber,
                                  AddressDTO address) {
}
