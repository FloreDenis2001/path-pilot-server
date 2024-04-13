package com.mycode.pathpilotserver.utils;

import com.mycode.pathpilotserver.packages.models.Package;

import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.shipments.dto.ShipmentDTO;
import com.mycode.pathpilotserver.shipments.models.Shipment;

import java.util.ArrayList;
import java.util.List;

public class Convertor {


    public static double convertToKm(double distance) {
        return distance / 1000;
    }

    public static ShipmentDTO convertToShipmentDTO(Shipment s) {

        ShipmentDTO shipmentDTO = ShipmentDTO.builder()
                .originName(s.getOriginName())
                .destinationName(s.getDestinationName())
                .originPhone(s.getOriginPhone())
                .destinationPhone(s.getDestinationPhone())
                .origin(s.getOriginAddress())
                .destination(s.getDestinationAddress())
                .status(s.getStatus().toString())
                .totalDistance(convertToKm(s.getTotalDistance()))
                .build();

        return shipmentDTO;
    }


    public static List<PackageRequest> convertToPackageRequest(List<Package> packages) {

        List<PackageRequest> packageRequests = new ArrayList<>();
        for (Package p : packages) {
            PackageRequest packageRequest = PackageRequest.builder()
                    .width(p.getWidth())
                    .height(p.getHeight())
                    .weight(p.getWeight())
                    .type(p.getType())
                    .totalAmount(p.getTotalAmount())
                    .deliveryDescription(p.getDeliveryDescription())
                    .shipmentDTO(convertToShipmentDTO(p.getShipment()))
                    .build();

            packageRequests.add(packageRequest);
        }

        return packageRequests;
    }
}
