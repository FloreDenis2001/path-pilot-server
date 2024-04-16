package com.mycode.pathpilotserver.packages.dto;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.packages.models.PackageType;
import com.mycode.pathpilotserver.shipments.dto.ShipmentDTO;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import lombok.Builder;

@Builder
public record PackageRequest(String awb,double width, double height, double weight, PackageType type , double totalAmount, String deliveryDescription,
                             ShipmentDTO shipmentDTO) {

}
