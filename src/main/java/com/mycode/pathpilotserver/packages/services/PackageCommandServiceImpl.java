package com.mycode.pathpilotserver.packages.services;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.exceptions.PackageAlreadyAssigned;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.mycode.pathpilotserver.city.utils.Utils.getCityByName;

@Service
public class PackageCommandServiceImpl implements PackageCommandService {

    private final PackageRepo packRepo;
    private final UserRepo customerRepo;
    private final ShipmentRepo shipmentRepo;

    private static final double DISTANCE_RATE = 1.2;
    private static final double WEIGHT_RATE = 0.9;
    private static final double VOLUME_RATE = 0.8;
    private static final double EARTH_RADIUS = 6371000;
    private static final int AWB_RANDOM_LETTERS_LENGTH = 8;

    public PackageCommandServiceImpl(PackageRepo packRepo, UserRepo customerRepo, ShipmentRepo shipmentRepo) {
        this.packRepo = packRepo;
        this.customerRepo = customerRepo;
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public void createPackage(PackageRequest packageRequest) {
        User customer = customerRepo.findByEmail(packageRequest.customerEmail())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with email: " + packageRequest.customerEmail() + " not found"));

        try {
            Shipment shipment = buildShipment(packageRequest);
            Package pack = buildPackage(packageRequest, customer, shipment);
            shipmentRepo.saveAndFlush(shipment);
            packRepo.saveAndFlush(pack);
        } catch (IOException e) {
            throw new RuntimeException("Error reading city data", e);
        }
    }

    @Override
    public void deletePackage(String awb) {
        Package pack = packRepo.getPackageByAwb(awb)
                .orElseThrow(() -> new PackageNotFoundException("Package with awb: " + awb + " not found"));
        packRepo.delete(pack);
    }

    @Override
    public void editPackage(String awb, PackageRequest packageRequest) {
        Package pack = packRepo.getPackageByAwb(awb)
                .orElseThrow(() -> new PackageNotFoundException("Package with awb: " + awb + " not found"));
        if (pack.getStatus() == PackageStatus.ASSIGNED) {
            throw new PackageAlreadyAssigned("Package with awb: " + awb + " is already assigned");
        }
        try {
            Shipment shipment = buildShipment(packageRequest);
            updateShipment(shipment, packageRequest);

            Package updatedPackage = editBuilderPackage(packageRequest, pack.getCustomer(), shipment);
            updatePackage(pack, updatedPackage);
        } catch (IOException e) {
            throw new RuntimeException("Error reading city data", e);
        }
    }

    private Shipment buildShipment(PackageRequest packageRequest) throws IOException {
        City origin = getCityByName(packageRequest.origin().addressDTO().city());
        City destination = getCityByName(packageRequest.destination().addressDTO().city());

        return Shipment.builder()
                .destinationName(packageRequest.destination().name())
                .originName(packageRequest.origin().name())
                .destinationPhone(packageRequest.destination().phone())
                .originPhone(packageRequest.origin().phone())
                .destinationAddress(buildAddress(destination, packageRequest.destination().addressDTO()))
                .originAddress(buildAddress(origin, packageRequest.origin().addressDTO()))
                .status(StatusType.PICKED)
                .estimatedDeliveryDate(LocalDateTime.now().plusDays(7))
                .totalDistance(calculateTotalDistance(origin, destination))
                .build();
    }

    private long calculateTotalDistance(City origin, City destination) {
        return Math.round(calculateDistance(origin.getLat(), origin.getLng(), destination.getLat(), destination.getLng()) / 1000);
    }

    private Address buildAddress(City city, AddressDTO addressDTO) {
        return Address.builder()
                .cityDetails(city)
                .street(addressDTO.street())
                .postalCode(addressDTO.postalCode())
                .streetNumber(addressDTO.streetNumber())
                .build();
    }

    private Package buildPackage(PackageRequest packageRequest, User customer, Shipment shipment) {
        double distance = shipment.getTotalDistance() / 1000;
        double weight = packageRequest.packageDetails().weight();
        double height = packageRequest.packageDetails().height();
        double width = packageRequest.packageDetails().width();
        double length = packageRequest.packageDetails().length();
        double totalAmount = calculateTotalAmount(distance, weight, height, width, length);

        return Package.builder()
                .customer((Customer) customer)
                .shipment(shipment)
                .awb(generateAWB(packageRequest))
                .totalAmount(totalAmount)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription(packageRequest.packageDetails().deliveryDescription())
                .orderDate(LocalDateTime.now())
                .height(height)
                .weight(weight)
                .width(width)
                .length(length)
                .build();
    }

    private Package editBuilderPackage(PackageRequest packageRequest, User customer, Shipment shipment) {
        return Package.builder()
                .customer((Customer) customer)
                .shipment(shipment)
                .awb(generateAWB(packageRequest))
                .totalAmount(packageRequest.packageDetails().totalAmount())
                .deliveryDescription(packageRequest.packageDetails().deliveryDescription())
                .orderDate(LocalDateTime.now())
                .height(packageRequest.packageDetails().height())
                .weight(packageRequest.packageDetails().weight())
                .width(packageRequest.packageDetails().width())
                .length(packageRequest.packageDetails().length())
                .build();
    }

    private double calculateTotalAmount(double distance, double weight, double height, double width, double length) {
        double volume = height * width * length / 1_000_000.0;
        return Math.round((distance * DISTANCE_RATE + weight * WEIGHT_RATE + volume * VOLUME_RATE) * 100.0) / 100.0;
    }

    private void updateShipment(Shipment shipment, PackageRequest packageRequest) throws IOException {
        City origin = getCityByName(packageRequest.origin().addressDTO().city());
        City destination = getCityByName(packageRequest.destination().addressDTO().city());

        shipment.setDestinationName(packageRequest.destination().name());
        shipment.setOriginName(packageRequest.origin().name());
        shipment.setDestinationPhone(packageRequest.destination().phone());
        shipment.setOriginPhone(packageRequest.origin().phone());
        shipment.setDestinationAddress(buildAddress(destination, packageRequest.destination().addressDTO()));
        shipment.setOriginAddress(buildAddress(origin, packageRequest.origin().addressDTO()));
        shipment.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
        shipment.setTotalDistance(calculateTotalDistance(origin, destination));
    }

    private void updatePackage(Package existingPackage, Package updatedPackage) {
        existingPackage.setLength(updatedPackage.getLength());
        existingPackage.setHeight(updatedPackage.getHeight());
        existingPackage.setWeight(updatedPackage.getWeight());
        existingPackage.setWidth(updatedPackage.getWidth());
        existingPackage.setDeliveryDescription(updatedPackage.getDeliveryDescription());
        existingPackage.setTotalAmount(updatedPackage.getTotalAmount());
        existingPackage.setOrderDate(LocalDateTime.now());
        existingPackage.setShipment(updatedPackage.getShipment());
        existingPackage.setStatus(PackageStatus.UNASSIGNED);
        packRepo.saveAndFlush(existingPackage);
    }

    private double calculateDistance(double originLat, double originLng, double destLat, double destLng) {
        double lat1 = Math.toRadians(originLat);
        double lon1 = Math.toRadians(originLng);
        double lat2 = Math.toRadians(destLat);
        double lon2 = Math.toRadians(destLng);
        double a = Math.pow(Math.sin((lat2 - lat1) / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon2 - lon1) / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private String generateAWB(PackageRequest packageRequest) {
        String cityAbbreviation = packageRequest.origin().addressDTO().city().substring(0, 2).toUpperCase();
        String randomLetters = RandomStringUtils.randomAlphabetic(AWB_RANDOM_LETTERS_LENGTH).toUpperCase();
        String currentTimeMillis = String.valueOf(System.currentTimeMillis()).substring(11, 13);
        return cityAbbreviation + randomLetters + currentTimeMillis;
    }
}
