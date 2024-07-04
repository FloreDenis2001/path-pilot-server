package com.mycode.pathpilotserver.company.dto;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import lombok.Builder;

@Builder
public record CompanyDTO(String registrationNumber, String name, String industry, double capital, String website,
                         String phone, String email,
                         AddressDTO address) {
}
