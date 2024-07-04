package com.mycode.pathpilotserver.address.dto;

import lombok.Builder;

@Builder
public record PackageAddress(String name, String phone, AddressDTO addressDTO) {
}
