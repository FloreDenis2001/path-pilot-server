package com.mycode.pathpilotserver.shipmentDetails.services;

import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShipmentDetailsServiceQuerry {

    Optional<ShipmentDetail> findByShipmentId(Long shipmentId);

    Optional<ShipmentDetail> findByArrivalTime(LocalDateTime arrivalTime);
}
