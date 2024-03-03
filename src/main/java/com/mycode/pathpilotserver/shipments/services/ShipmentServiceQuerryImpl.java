package com.mycode.pathpilotserver.shipments.services;

import com.mycode.pathpilotserver.address.Address;
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
    public Optional<Shipment> findByOrigin(Address origin) {
        Optional<Shipment> shipment = shipmentRepo.findShipmentByOriginAddress(origin);
        if (shipment.isPresent()) {
            return shipment;
        } else {
            throw new ShipmentNotFoundException("Shipment with origin: " + origin + " not found");
        }
    }

    @Override
    public Optional<Shipment> findByDestination(Address destination) {
        Optional<Shipment> shipment = shipmentRepo.findShipmentByDestinationAddress(destination);
        if (shipment.isPresent()) {
            return shipment;
        } else {
            throw new ShipmentNotFoundException("Shipment with destination: " + destination + " not found");
        }
    }


}
