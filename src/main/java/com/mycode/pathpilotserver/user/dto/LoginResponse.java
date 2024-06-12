package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.system.security.UserRole;
import lombok.Builder;

@Builder
public record LoginResponse(Long id, String username, String firstName, String lastName, UserRole role, String email,
                            String phone,
                            String companyRegistrationNumber,AddressDTO addressDTO, String token ) {
}