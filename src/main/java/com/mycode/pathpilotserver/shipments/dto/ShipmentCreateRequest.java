package com.mycode.pathpilotserver.shipments.dto;

import com.mycode.pathpilotserver.address.Address;

public record ShipmentCreateRequest(Address origin,Address destination) {
}
