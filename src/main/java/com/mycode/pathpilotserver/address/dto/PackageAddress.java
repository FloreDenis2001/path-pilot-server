package com.mycode.pathpilotserver.address.dto;

import com.mycode.pathpilotserver.address.models.Address;
import lombok.Builder;

@Builder
public record PackageAddress(String name, String phone, Address address) {
}
