package com.mycode.pathpilotserver.packages.services;

import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.intercom.maps.DirectionsService;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.models.PackageStatus;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class PackageCommandServiceImpl implements PackageCommandService {

    private final PackageRepo packRepo;
    private final UserRepo customerRepo;

//    private static DirectionsService directionsService = new DirectionsService();

    private final ShipmentRepo shipmentRepo;

    public PackageCommandServiceImpl(PackageRepo packRepo, UserRepo customerRepo, DirectionsService directionsService, ShipmentRepo shipmentRepo) {
        this.packRepo = packRepo;
        this.customerRepo = customerRepo;
//        this.directionsService = directionsService;
        this.shipmentRepo = shipmentRepo;
    }


    private static Optional<Shipment> getShipments(PackageRequest packageRequest) {
        try {

//            DirectionsResult directionsResult = directionsService.getDirections(packageDTO.origin().toString(),packageDTO.destination().toString());
//
//            double totalDistanceInKm = 0.0;
//            for (int i = 0; i < directionsResult.routes.length; i++) {
//                totalDistanceInKm += directionsResult.routes[i].legs[0].distance.inMeters;
//            }

            Shipment shipment = new Shipment().builder().destinationName(packageRequest.destination().name())
                    .originName(packageRequest.origin().name())
                    .destinationPhone(packageRequest.destination().phone())
                    .originPhone(packageRequest.origin().phone())
                    .destinationAddress(packageRequest.destination().address())
                    .originAddress(packageRequest.origin().address())
                    .status(StatusType.PICKED)
                    .estimatedDeliveryDate(LocalDateTime.now().plusDays(3))
                    .totalDistance(0)
                    .build();

            return Optional.of(shipment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Package getPackage(PackageRequest packageRequest, Optional<User> customer, Optional<Shipment> shipment) {

        Package pack = new Package().builder()
                .customer((Customer) customer.get())
                .shipment(shipment.get())
                .awb((packageRequest.origin().address().getCity().substring(0, 2).concat(RandomStringUtils.randomAlphabetic(8).concat(String.valueOf(System.currentTimeMillis()).substring(11, 13)))).toUpperCase())
                .totalAmount(packageRequest.packageDetails().totalAmount())
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription(packageRequest.packageDetails().deliveryDescription())
                .orderDate(LocalDateTime.now())
                .height(packageRequest.packageDetails().height())
                .weight(packageRequest.packageDetails().weight())
                .width(packageRequest.packageDetails().width())
                .length(packageRequest.packageDetails().length())
                .build();

        return pack;
    }


    @Override
    public void createPackage(PackageRequest packageRequest) {

        Optional<User> customer = customerRepo.findByEmail(packageRequest.customerEmail());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer with email: " + packageRequest.customerEmail() + " not found");
        }

        Optional<Shipment> shipment = getShipments(packageRequest);
        Package pack = getPackage(packageRequest, customer, shipment);


        shipmentRepo.saveAndFlush(shipment.get());
        packRepo.saveAndFlush(pack);


    }

    @Override
    public void deletePackage(String awb) {
        Optional<Package> pack = packRepo.getPackageByAwb(awb);
        if (pack.isEmpty()) {
            throw new CustomerNotFoundException("Package with awb: " + awb + " not found");
        }
        packRepo.delete(pack.get());
    }

    @Override
    public void editPackage(String awb, PackageRequest packageRequest) {
        Optional<Package> pack = packRepo.getPackageByAwb(awb);

        if (pack.isEmpty()) {
            throw new CustomerNotFoundException("Package with awb: " + awb + " not found");
        }
        Optional<Shipment> shipment = shipmentRepo.findById(pack.get().getShipment().getId());

        shipment.get().setDestinationName(packageRequest.destination().name());
        shipment.get().setOriginName(packageRequest.origin().name());
        shipment.get().setDestinationPhone(packageRequest.destination().phone());
        shipment.get().setOriginPhone(packageRequest.origin().phone());
        shipment.get().setDestinationAddress(packageRequest.destination().address());
        shipment.get().setOriginAddress(packageRequest.origin().address());
        shipment.get().setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
        shipment.get().setTotalDistance(0);

        shipmentRepo.saveAndFlush(shipment.get());

        pack.get().setLength(packageRequest.packageDetails().length());
        pack.get().setHeight(packageRequest.packageDetails().height());
        pack.get().setWeight(packageRequest.packageDetails().weight());
        pack.get().setWidth(packageRequest.packageDetails().width());
        pack.get().setDeliveryDescription(packageRequest.packageDetails().deliveryDescription());
        pack.get().setTotalAmount(packageRequest.packageDetails().totalAmount());
        pack.get().setOrderDate(LocalDateTime.now());
        pack.get().setShipment(shipment.get());
        pack.get().setStatus(PackageStatus.UNASSIGNED);

        packRepo.saveAndFlush(pack.get());


    }
}
