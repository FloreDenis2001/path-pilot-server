package com.mycode.pathpilotserver.shipments.dto;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import lombok.Builder;

@Builder
public record ShipmentDTO(String originName, String destinationName, String originPhone, String destinationPhone, AddressDTO origin, AddressDTO destination, String status, double totalDistance) {

}
