package com.mycode.pathpilotserver.packages.services;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.intercom.maps.DirectionsService;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.models.PackageType;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class PackageCommandServiceImpl implements PackageCommandService {

    private final PackageRepo packRepo;
    private final UserRepo customerRepo;

    private static DirectionsService directionsService = new DirectionsService();

    private final ShipmentRepo shipmentRepo;

    public PackageCommandServiceImpl(PackageRepo packRepo, UserRepo customerRepo, DirectionsService directionsService, ShipmentRepo shipmentRepo) {
        this.packRepo = packRepo;
        this.customerRepo = customerRepo;
        this.directionsService = directionsService;
        this.shipmentRepo = shipmentRepo;
    }


    private static Optional<Shipment> getShipments(PackageDTO packageDTO)  {
        try {

//            DirectionsResult directionsResult = directionsService.getDirections(packageDTO.origin().toString(),packageDTO.destination().toString());
//
//            double totalDistanceInKm = 0.0;
//            for (int i = 0; i < directionsResult.routes.length; i++) {
//                totalDistanceInKm += directionsResult.routes[i].legs[0].distance.inMeters;
//            }

            Shipment shipment = new Shipment().builder().destinationName(packageDTO.destinationName())
                    .originName(packageDTO.originName())
                    .destinationPhone(packageDTO.destinationPhone())
                    .originPhone(packageDTO.originPhone())
                    .destinationAddress(packageDTO.destination())
                    .originAddress(packageDTO.origin())
                    .status(StatusType.PICKED)
                    .estimatedDeliveryDate(LocalDateTime.now().plusDays(3))
                    .totalDistance(0)
                    .build();

            return Optional.of(shipment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Package getPackage(PackageDTO packageDTO, Optional<User> customer, Optional<Shipment> shipment) {

        Package pack = new Package().builder()
                .customer((Customer) customer.get())
                .shipment(shipment.get())
                .totalAmount(packageDTO.totalAmount())
                .type(PackageType.UNASSIGNED)
                .deliveryDescription(packageDTO.deliveryDescription())
                .orderDate(LocalDateTime.now())
                .height(packageDTO.height())
                .weight(packageDTO.weight())
                .width(packageDTO.width())
                .build();

        return pack;
    }


    @Override
    public void createPackage(PackageDTO packageDTO) {

        Optional<User> customer = customerRepo.findById(packageDTO.customerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer with id: " + packageDTO.customerId() + " not found");
        }

        Optional<Shipment> shipment = getShipments(packageDTO);
        Package pack = getPackage(packageDTO, customer, shipment);


        shipmentRepo.saveAndFlush(shipment.get());
        packRepo.saveAndFlush(pack);


    }
}
