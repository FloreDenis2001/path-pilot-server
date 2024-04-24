package com.mycode.pathpilotserver.packages.dto;

import com.mycode.pathpilotserver.address.dto.PackageAddress;
import lombok.Builder;

@Builder
public record PackageRequest(String customerEmail, PackageDetails packageDetails, PackageAddress origin, PackageAddress destination){

}
