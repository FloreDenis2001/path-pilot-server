package com.mycode.pathpilotserver.packages.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mycode.pathpilotserver.city.utils.Utils.readCitiesFromJsonFile;

@Service
public class
PackageCommandServiceImpl implements PackageCommandService {

    private final PackageRepo packRepo;
    private final UserRepo customerRepo;
    private final ShipmentRepo shipmentRepo;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final double DISTANCE_RATE = 1.2;
    private static final double WEIGHT_RATE = 0.9;
    private static final double VOLUME_RATE = 0.8;
    private static final double EARTH_RADIUS = 6371000;
    private static final int AWB_RANDOM_LETTERS_LENGTH = 8;

    private static final List<City> cities;

    static {
        try {
            cities = readCitiesFromJsonFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PackageCommandServiceImpl(PackageRepo packRepo, UserRepo customerRepo, ShipmentRepo shipmentRepo) {
        this.packRepo = packRepo;
        this.customerRepo = customerRepo;
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public void createPackage(PackageRequest packageRequest) {
        Optional<User> customer = customerRepo.findByEmail(packageRequest.customerEmail());
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer with email: " + packageRequest.customerEmail() + " not found");
        }

        try {
            Shipment shipment = buildShipment(packageRequest);
            Package pack = buildPackage(packageRequest, customer.get(), shipment);

            shipmentRepo.saveAndFlush(shipment);
            packRepo.saveAndFlush(pack);
        } catch (IOException e) {
            throw new RuntimeException("Error reading city data", e);
        }
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

        try {
            Shipment shipment = buildShipment(packageRequest);
            updateShipment(shipment, packageRequest);

            Package updatedPackage = editBuilderPackage(packageRequest, pack.get().getCustomer(), shipment);
            updatePackage(pack.get(), updatedPackage);
        } catch (IOException e) {
            throw new RuntimeException("Error reading city data", e);
        }
    }

    private Shipment buildShipment(PackageRequest packageRequest) throws IOException {
        City origin = getCityByName(packageRequest.origin().addressDTO().city());
        City destination = getCityByName(packageRequest.destination().addressDTO().city());

        Address fullDestinationAddress = buildAddress(destination, packageRequest.destination().addressDTO());
        Address fullOriginAddress = buildAddress(origin, packageRequest.origin().addressDTO());

        return Shipment.builder()
                .destinationName(packageRequest.destination().name())
                .originName(packageRequest.origin().name())
                .destinationPhone(packageRequest.destination().phone())
                .originPhone(packageRequest.origin().phone())
                .destinationAddress(fullDestinationAddress)
                .originAddress(fullOriginAddress)
                .status(StatusType.PICKED)
                .estimatedDeliveryDate(LocalDateTime.now().plusDays(3))
                .totalDistance(calculateTotalDistance(origin, destination))
                .build();
    }

    private long calculateTotalDistance(City origin, City destination) {
        double totalDistanceInMeters = calculateDistance(origin.getLat(), origin.getLng(), destination.getLat(), destination.getLng());
        return Math.round(totalDistanceInMeters / 1000);
    }

    private Address buildAddress(City city, AddressDTO addressDTO) {
        return Address.builder()
                .city(city.getCity())
                .country(addressDTO.country())
                .street(addressDTO.street())
                .postalCode(addressDTO.postalCode())
                .streetNumber(addressDTO.streetNumber())
                .lat(city.getLat())
                .lng(city.getLng())
                .admin_name(city.getAdmin_name())
                .capital(city.getCapital())
                .iso2(city.getIso2())
                .population(city.getPopulation())
                .population_proper(city.getPopulation_proper())
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
                .height(packageRequest.packageDetails().height())
                .weight(packageRequest.packageDetails().weight())
                .width(packageRequest.packageDetails().width())
                .length(packageRequest.packageDetails().length())
                .build();
    }

    private Package editBuilderPackage(PackageRequest packageRequest, User customer, Shipment shipment) {
        return Package.builder()
                .customer((Customer) customer)
                .shipment(shipment)
                .awb(generateAWB(packageRequest))
                .totalAmount(packageRequest.packageDetails().totalAmount())
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription(packageRequest.packageDetails().deliveryDescription())
                .orderDate(LocalDateTime.now())
                .height(packageRequest.packageDetails().height())
                .weight(packageRequest.packageDetails().weight())
                .width(packageRequest.packageDetails().width())
                .length(packageRequest.packageDetails().length())
                .build();
    }

    private double calculateTotalAmount(double distance, double weight, double height, double width, double length) {
        double volume = height * width * length / 1000000.0;
        double value = distance * DISTANCE_RATE + weight * WEIGHT_RATE + volume * VOLUME_RATE;
        return Double.parseDouble(String.format("%.2f", value));
    }

    private void updateShipment(Shipment shipment, PackageRequest packageRequest) throws IOException {
        City origin = getCityByName(packageRequest.origin().addressDTO().city());
        City destination = getCityByName(packageRequest.destination().addressDTO().city());
        Address fullDestinationAddress = buildAddress(destination, packageRequest.destination().addressDTO());
        Address fullOriginAddress = buildAddress(origin, packageRequest.origin().addressDTO());
        shipment.setDestinationName(packageRequest.destination().name());
        shipment.setOriginName(packageRequest.origin().name());
        shipment.setDestinationPhone(packageRequest.destination().phone());
        shipment.setOriginPhone(packageRequest.origin().phone());
        shipment.setDestinationAddress(fullDestinationAddress);
        shipment.setOriginAddress(fullOriginAddress);
        shipment.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
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


    private City getCityByName(String cityName) {
        return cities.stream()
                .filter(city -> city.getCity().equals(cityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + cityName));
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
        String cityAbbreviation = packageRequest.origin().addressDTO().city().substring(0, 2);
        String randomLetters = RandomStringUtils.randomAlphabetic(AWB_RANDOM_LETTERS_LENGTH);
        String currentTimeMillis = String.valueOf(System.currentTimeMillis()).substring(11, 13);
        return (cityAbbreviation + randomLetters + currentTimeMillis).toUpperCase();
    }
}