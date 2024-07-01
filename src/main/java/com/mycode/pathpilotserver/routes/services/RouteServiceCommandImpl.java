package com.mycode.pathpilotserver.routes.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;

import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class RouteServiceCommandImpl implements RouteServiceCommand {

    private final PackageRepo packageRepo;
    private final VehicleRepo vehicleRepo;
    private final DriverRepo driverRepo;
    private final RouteRepo routeRepo;

    private static final List<City> cities = new ArrayList<>();

    static {
        try {
            getAllCities();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public RouteServiceCommandImpl(PackageRepo packageRepo,
                                   VehicleRepo vehicleRepo,
                                   DriverRepo driverRepo,
                                   RouteRepo routeRepo) {
        this.packageRepo = packageRepo;
        this.vehicleRepo = vehicleRepo;
        this.driverRepo = driverRepo;
        this.routeRepo = routeRepo;
    }

    @Override
    @Transactional
    public void generateRoute(String companyRegistrationNumber) throws IOException {

    }

    //todo : SORT PACAKGE BY DISTANCE WITH ORIGIN
    public List<Package> sortPackagesByDistance(List<Package> packages, City origin) {
        packages.sort(Comparator.comparingDouble(p -> calculateDistance(getCityByName(p.getShipment().getDestinationAddress().getCity(), cities), origin)));
        return packages;
    }


    //todo :  GET CITY BY NAME FROM LIST
    private City getCityByName(String cityName, List<City> cities) {
        return cities.stream()
                .filter(city -> city.getCity().equals(cityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + cityName));
    }

    //todo : GET ALL CITIES FROM JSON FILE AND ADD TO LIST
    public static void getAllCities() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        cities.addAll(objectMapper.readValue(new File("C:\\Users\\denis\\OneDrive\\Desktop\\LUCRARE LICENTA\\path-pilot-server\\src\\main\\java\\com\\mycode\\pathpilotserver\\resource\\ro.json"), new TypeReference<List<City>>() {
        }));
    }

    private double calculateDistance(City origin, City destination) {
        double lat1 = Math.toRadians(origin.getLat());
        double lon1 = Math.toRadians(origin.getLng());
        double lat2 = Math.toRadians(destination.getLat());
        double lon2 = Math.toRadians(destination.getLng());

        double earthRadius = 6371000;
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

}