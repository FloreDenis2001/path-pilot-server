package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.system.security.UserRole;
import lombok.Builder;

@Builder
public record LoginResponse(Long id, String username, String firstName, String lastName, UserRole role, String email,
                            String phone,
                            String companyRegistrationNumber, String token) {
}