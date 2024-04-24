package com.mycode.pathpilotserver.packages.dto;

import com.mycode.pathpilotserver.address.dto.PackageAddress;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.packages.models.PackageStatus;
import com.mycode.pathpilotserver.shipments.dto.ShipmentDTO;
import lombok.Builder;

@Builder
public record PackageDTO(String customerEmail, PackageStatus status, String awb, PackageDetails packageDetails, ShipmentDTO shipmentDTO) {
}