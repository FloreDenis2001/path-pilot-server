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




}
