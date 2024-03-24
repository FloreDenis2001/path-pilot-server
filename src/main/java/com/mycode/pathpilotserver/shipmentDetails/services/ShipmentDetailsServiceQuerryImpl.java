package com.mycode.pathpilotserver.shipmentDetails.services;

import com.mycode.pathpilotserver.shipmentDetails.repository.ShipmentDetailsRepo;

public class ShipmentDetailsServiceQuerryImpl implements ShipmentDetailsServiceQuerry {
    private final ShipmentDetailsRepo shipmentDetailsRepo;

    public ShipmentDetailsServiceQuerryImpl(ShipmentDetailsRepo shipmentDetailsRepo) {
        this.shipmentDetailsRepo = shipmentDetailsRepo;
    }




}
