package com.mycode.pathpilotserver.shipments.services;

import com.mycode.pathpilotserver.shipments.exceptions.ShipmentNotFoundException;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;

import java.util.Optional;

public class ShipmentServiceQuerryImpl implements ShipmentServiceQuerry{
    private final ShipmentRepo shipmentRepo;

    public ShipmentServiceQuerryImpl(ShipmentRepo shipmentRepo) {
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public Optional<Shipment> findByOrigin(String origin) {
        Optional<Shipment> shipment = shipmentRepo.findByOrigin(origin);
        if (shipment.isPresent()) {
            return shipment;
        } else {
            throw new ShipmentNotFoundException("Shipment with origin: " + origin + " not found");
        }
    }
}
