package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;

public record RegisterDTO(RegisterUserRequest user, CompanyCreateRequest company) {
}
