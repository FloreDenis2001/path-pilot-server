package com.mycode.pathpilotserver.routes.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.models.PackageStatus;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RouteServiceCommandImpl implements RouteServiceCommand {

    private final PackageRepo packageRepo;
    private final VehicleRepo vehicleRepo;
    private final DriverRepo driverRepo;
    private final OrderRepo orderRepo;
    private final RouteRepo routeRepo;

    private final ObjectMapper objectMapper;

    public RouteServiceCommandImpl(PackageRepo packageRepo,
                                   VehicleRepo vehicleRepo,
                                   DriverRepo driverRepo,
                                   OrderRepo orderRepo,
                                   RouteRepo routeRepo, ObjectMapper objectMapper) {
        this.packageRepo = packageRepo;
        this.vehicleRepo = vehicleRepo;
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
        this.routeRepo = routeRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void generateRoute(String companyRegistrationNumber) throws IOException {
        List<Package> packageList = getUnassignedPackages(companyRegistrationNumber);

        List<Vehicle> suitableVehicles = getInactiveVehicles(companyRegistrationNumber);
        List<Driver> drivers = getAvailableDrivers(companyRegistrationNumber);

        Map<Vehicle, List<Package>> vehiclePackageMap = findOptimalRoutes(packageList, suitableVehicles);

        assignPackagesToRoutes(vehiclePackageMap, drivers);
    }

    private void assignPackagesToRoutes(Map<Vehicle, List<Package>> vehiclePackageMap, List<Driver> drivers) throws IOException {
        int driverIndex = 0;

        for (Map.Entry<Vehicle, List<Package>> entry : vehiclePackageMap.entrySet()) {
            if (driverIndex >= drivers.size()) {
                throw new RuntimeException("Not enough drivers available for all routes");
            }

            Vehicle vehicle = entry.getKey();
            List<Package> routePackages = entry.getValue();
            Driver driver = drivers.get(driverIndex);

            assignPackagesToRoute(routePackages, driver, vehicle);
            driverIndex++;
        }
    }

    private void assignPackagesToRoute(List<Package> routePackages, Driver driver, Vehicle selectedVehicle) throws IOException {
        Set<String> packageCities = new HashSet<>();
        for (Package p : routePackages) {
            packageCities.add(p.getShipment().getDestinationAddress().getCity());
        }

        List<String> cities = new ArrayList<>(packageCities);
        String startCity = "Bucuresti";

        List<String> optimalRoute = generateOptimalRoute(startCity, cities);

        Route route = new Route();
        route.setVehicle(selectedVehicle);
        route.setDriver(driver);
        route.setCompany(selectedVehicle.getCompany());
        route.setArrivalTime(LocalDateTime.now().plusDays(6));

        LocalDateTime departureTime = LocalDateTime.now().plusHours(3);
        route.setDepartureDate(departureTime);

        double totalTravelDistance = 0.0;
        City previousCity = getCityByName(startCity, readCitiesFromJsonFile());

        for (String city : optimalRoute) {
            City currentCity = getCityByName(city, readCitiesFromJsonFile());
            if (!currentCity.equals(previousCity)) {
                totalTravelDistance += calculateDistance(previousCity, currentCity);
            }
            for (Package p : routePackages) {
                if (p.getShipment().getDestinationAddress().getCity().equals(city)) {
                    Order order = Convertor.convertPackageToOrder(p);
                    route.addOrder(order);
                    System.out.println("Package " + p.toString());
                }
            }
            previousCity = currentCity;
        }

        route.setTotalDistance(Double.parseDouble(String.format("%.2f", totalTravelDistance / 1000)));


        for (Package p : routePackages) {
            p.setStatus(PackageStatus.ASSIGNED);
            packageRepo.saveAndFlush(p);
        }

        routeRepo.saveAndFlush(route);
        System.out.println("Route generated: " + route.toString());
    }

    private Map<Vehicle, List<Package>> findOptimalRoutes(List<Package> packages, List<Vehicle> vehicles) throws IOException {
        Map<Vehicle, List<Package>> vehiclePackageMap = new HashMap<>();

        double totalVolume = packages.stream().mapToDouble(Package::getVolume).sum();

        vehicles.sort(Comparator.comparingDouble(Vehicle::getVolume).reversed());
        packages.sort(Comparator.comparingDouble(Package::getVolume).reversed());

        if (totalVolume <= vehicles.get(0).getVolume()) {
            vehiclePackageMap.put(vehicles.get(0), packages);
            return vehiclePackageMap;
        }

        int packageIndex = 0;
        for (Vehicle vehicle : vehicles) {
            List<Package> vehiclePackages = new ArrayList<>();
            double currentVolume = 0.0;

            while (packageIndex < packages.size() && currentVolume + packages.get(packageIndex).getVolume() <= vehicle.getVolume()) {
                vehiclePackages.add(packages.get(packageIndex));
                currentVolume += packages.get(packageIndex).getVolume();
                packageIndex++;
            }

            if (!vehiclePackages.isEmpty()) {
                vehiclePackageMap.put(vehicle, vehiclePackages);
                totalVolume -= vehiclePackages.stream().mapToDouble(Package::getVolume).sum();
            }

            if (totalVolume == 0) {
                break;
            }
        }

        if (packageIndex < packages.size()) {
            Vehicle lastVehicle = vehicles.get(vehicles.size() - 1);
            List<Package> lastVehiclePackages = new ArrayList<>();
            double currentVolume = 0.0;

            while (packageIndex < packages.size()) {
                Package packageToAdd = packages.get(packageIndex);
                if (currentVolume + packageToAdd.getVolume() <= lastVehicle.getVolume()) {
                    lastVehiclePackages.add(packageToAdd);
                    currentVolume += packageToAdd.getVolume();
                    packageIndex++;
                } else {
                    break;
                }
            }

            if (!lastVehiclePackages.isEmpty()) {
                vehiclePackageMap.put(lastVehicle, lastVehiclePackages);
            }
        }

        while (totalVolume > 0) {
            Vehicle newVehicle = vehicles.get(vehicles.size() - 1);
            List<Package> newVehiclePackages = new ArrayList<>();
            double currentVolume = 0.0;

            for (int i = packageIndex; i < packages.size(); i++) {
                Package packageToAdd = packages.get(i);
                if (currentVolume + packageToAdd.getVolume() <= newVehicle.getVolume()) {
                    newVehiclePackages.add(packageToAdd);
                    currentVolume += packageToAdd.getVolume();
                    packageIndex++;
                } else {
                    break;
                }
            }

            if (!newVehiclePackages.isEmpty()) {
                vehiclePackageMap.put(newVehicle, newVehiclePackages);
                totalVolume -= newVehiclePackages.stream().mapToDouble(Package::getVolume).sum();
            }
        }

        System.out.println("findOptimalRoutes: vehiclePackageMap before VND: " + vehiclePackageMap);
        vnd(vehiclePackageMap, vehicles);
        System.out.println("findOptimalRoutes: vehiclePackageMap after VND: " + vehiclePackageMap);

        return vehiclePackageMap;
    }

    private void vnd(Map<Vehicle, List<Package>> vehiclePackageMap, List<Vehicle> vehicles) throws IOException {
        boolean improvement = true;
        while (improvement) {
            improvement = false;

            for (int i = 0; i < vehicles.size(); i++) {
                for (int j = i + 1; j < vehicles.size(); j++) {
                    List<Package> packages1 = vehiclePackageMap.get(vehicles.get(i));
                    List<Package> packages2 = vehiclePackageMap.get(vehicles.get(j));

                    if (packages1 != null && packages2 != null) {
                        if (swapPackages(vehiclePackageMap, vehicles.get(i), vehicles.get(j))) {
                            improvement = true;
                        }
                    }
                }
            }

            for (Vehicle vehicle : vehicles) {
                List<Package> packages = vehiclePackageMap.get(vehicle);
                if (packages != null && !packages.isEmpty()) {
                    if (twoOptSwap(packages)) {
                        improvement = true;
                    }
                }
            }
        }
    }

    private boolean swapPackages(Map<Vehicle, List<Package>> vehiclePackageMap, Vehicle vehicle1, Vehicle vehicle2) throws IOException {
        List<Package> packages1 = vehiclePackageMap.get(vehicle1);
        List<Package> packages2 = vehiclePackageMap.get(vehicle2);

        if (packages1 == null || packages2 == null) {
            System.out.println("swapPackages: One of the package lists is null. packages1: " + packages1 + ", packages2: " + packages2);
            return false;
        }

        boolean improved = false;
        for (int i = 0; i < packages1.size(); i++) {
            for (int j = 0; j < packages2.size(); j++) {
                Package temp = packages1.get(i);
                packages1.set(i, packages2.get(j));
                packages2.set(j, temp);

                double currentCost = calculateRouteCost(packages1) + calculateRouteCost(packages2);
                double newCost = calculateRouteCost(packages1) + calculateRouteCost(packages2);

                if (newCost < currentCost) {
                    improved = true;
                } else {
                    temp = packages1.get(i);
                    packages1.set(i, packages2.get(j));
                    packages2.set(j, temp);
                }
            }
        }
        return improved;
    }

    private boolean twoOptSwap(List<Package> packages) throws IOException {
        boolean improved = false;
        for (int i = 1; i < packages.size() - 1; i++) {
            for (int j = i + 1; j < packages.size(); j++) {
                List<Package> newRoute = new ArrayList<>(packages.subList(0, i));
                List<Package> reversedSubList = new ArrayList<>(packages.subList(i, j + 1));
                Collections.reverse(reversedSubList);
                newRoute.addAll(reversedSubList);
                newRoute.addAll(packages.subList(j + 1, packages.size()));

                double currentCost = calculateRouteCost(packages);
                double newCost = calculateRouteCost(newRoute);

                if (newCost < currentCost) {
                    packages.clear();
                    packages.addAll(newRoute);
                    improved = true;
                }
            }
        }
        return improved;
    }

    private double calculateRouteCost(List<Package> packages) throws IOException {
        double cost = 0.0;
        String currentCity = "Bucuresti";
        List<City> cities = readCitiesFromJsonFile();

        City cityOrigin = getCityByName(currentCity, cities);

        for (Package p : packages) {
            City destinationCity = getCityByName(p.getShipment().getDestinationAddress().getCity(), cities);
            cost += calculateDistance(cityOrigin, destinationCity);
            cityOrigin = destinationCity;
        }

        return cost;
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

    private List<String> generateOptimalRoute(String startCity, List<String> cities) {
        List<String> route = new ArrayList<>(cities);
        Collections.shuffle(route);
        route.add(0, startCity);
        route.add(startCity);
        return route;
    }

    private City getCityByName(String cityName, List<City> cities) {
        return cities.stream()
                .filter(city -> city.getCity().equals(cityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + cityName));
    }

    private List<City> readCitiesFromJsonFile() throws IOException {
        File jsonFile = new File("C:\\Users\\denis\\OneDrive\\Desktop\\LUCRARE LICENTA\\path-pilot-server\\src\\main\\java\\com\\mycode\\pathpilotserver\\resource\\ro.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<City> cities = objectMapper.readValue(jsonFile, new TypeReference<List<City>>() {});
        return cities;
    }

    private List<Package> getUnassignedPackages(String companyRegistrationNumber) {
        return packageRepo.getAllUnassignedPackages(companyRegistrationNumber)
                .orElseThrow(() -> new PackageNotFoundException("No unassigned packages found for company with registration number " + companyRegistrationNumber));
    }

    private List<Vehicle> getInactiveVehicles(String companyRegistrationNumber) {
        return vehicleRepo.getInactiveVehiclesByCompanyRegistrationNumber(companyRegistrationNumber)
                .orElseThrow(() -> new VehicleNotFoundException("No vehicles found for company with registration number " + companyRegistrationNumber));
    }

    private List<Driver> getAvailableDrivers(String companyRegistrationNumber) {
        List<Driver> drivers = driverRepo.findAllByCompanyRegistrationNumberAndIsAvailableTrue(companyRegistrationNumber).orElse(new ArrayList<>());
        if (drivers.isEmpty()) {
            throw new RuntimeException("No available drivers found for the company");
        }
        return drivers;
    }
}