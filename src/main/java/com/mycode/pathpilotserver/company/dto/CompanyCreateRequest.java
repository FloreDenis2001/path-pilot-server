package com.mycode.pathpilotserver.company.dto;

import com.mycode.pathpilotserver.address.models.Address;

public record CompanyCreateRequest(String name, String industry, double capital,
                                   String registrationNumber, String website,
                                   Address address, String phone, String email) {
}
