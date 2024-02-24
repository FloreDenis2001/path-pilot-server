package com.mycode.pathpilotserver.shipments.services;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.shipments.models.Shipment;

import java.util.Optional;

public interface ShipmentServiceQuerry {

    Optional<Shipment> findByOrigin(Address origin);
}
