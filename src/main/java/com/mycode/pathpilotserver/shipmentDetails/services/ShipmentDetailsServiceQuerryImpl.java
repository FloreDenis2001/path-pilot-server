package com.mycode.pathpilotserver.shipmentDetails.services;

import com.mycode.pathpilotserver.shipmentDetails.exceptions.ShipmentDetailsNotFoundException;
import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import com.mycode.pathpilotserver.shipmentDetails.repository.ShipmentDetailsRepo;

import java.time.LocalDateTime;
import java.util.Optional;

public class ShipmentDetailsServiceQuerryImpl implements ShipmentDetailsServiceQuerry {
    private final ShipmentDetailsRepo shipmentDetailsRepo;

    public ShipmentDetailsServiceQuerryImpl(ShipmentDetailsRepo shipmentDetailsRepo) {
        this.shipmentDetailsRepo = shipmentDetailsRepo;
    }


    @Override
    public Optional<ShipmentDetail> findByShipmentId(Long shipmentId) {
        Optional<ShipmentDetail> shipmentDetail = shipmentDetailsRepo.findByShipmentId(shipmentId);
        if (shipmentDetail.isPresent()) {
            return shipmentDetail;
        } else {
            throw new ShipmentDetailsNotFoundException("ShipmentDetail with shipment id: " + shipmentId + " not found");
        }
    }

    @Override
    public Optional<ShipmentDetail> findByArrivalTime(LocalDateTime arrivalTime) {
        Optional<ShipmentDetail> shipmentDetail = shipmentDetailsRepo.findByArrivalTime(arrivalTime);
        if (shipmentDetail.isPresent()) {
            return shipmentDetail;
        } else {
            throw new ShipmentDetailsNotFoundException("ShipmentDetail with arrival time: " + arrivalTime + " not found");
        }
    }
}
