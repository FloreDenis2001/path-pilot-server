package com.mycode.pathpilotserver.shipments.dto;

import com.mycode.pathpilotserver.address.models.Address;
import lombok.Builder;

@Builder
public record ShipmentDTO(String originName, String destinationName, String originPhone, String destinationPhone, Address origin, Address destination,String status,double totalDistance) {

}
