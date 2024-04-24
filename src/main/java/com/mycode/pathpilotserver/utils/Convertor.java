package com.mycode.pathpilotserver.utils;

import com.mycode.pathpilotserver.address.dto.PackageAddress;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageDetails;
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


    public static List<PackageDTO> convertToPackageDTO(List<Package> packages) {

        List<PackageDTO> packageDTOS = new ArrayList<>();
        for (Package p : packages) {
            PackageDetails packageDetails = PackageDetails.builder()
                    .totalAmount(p.getTotalAmount())
                    .weight(p.getWeight())
                    .height(p.getHeight())
                    .length(p.getLength())
                    .width(p.getWidth())
                    .deliveryDescription(p.getDeliveryDescription())
                    .build();

            ShipmentDTO shipmentDTO = convertToShipmentDTO(p.getShipment());

            PackageDTO packageDTO = PackageDTO.builder()
                    .customerEmail(p.getCustomer().getEmail())
                    .awb(p.getAwb())
                    .status(p.getStatus())
                    .packageDetails(packageDetails)
                    .shipmentDTO(shipmentDTO)
                    .build();

            packageDTOS.add(packageDTO);
        }

        return packageDTOS;
    }

}
