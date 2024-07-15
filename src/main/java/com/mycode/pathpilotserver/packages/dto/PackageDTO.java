package com.mycode.pathpilotserver.packages.dto;

import com.mycode.pathpilotserver.system.enums.PackageStatus;
import com.mycode.pathpilotserver.shipments.dto.ShipmentDTO;
import lombok.Builder;

@Builder
public record PackageDTO(String customerEmail, PackageStatus status, String awb, PackageDetails packageDetails, ShipmentDTO shipmentDTO) {
}