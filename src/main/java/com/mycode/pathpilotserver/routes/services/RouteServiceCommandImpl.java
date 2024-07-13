package com.mycode.pathpilotserver.routes.services;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.google.ortools.util.OptionalBoolean;
import com.google.protobuf.Duration;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.orders.models.Order;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.mycode.pathpilotserver.city.utils.Utils.getCityByName;

@Service
public class RouteServiceCommandImpl implements RouteServiceCommand {

    private final PackageRepo packageRepo;
    private final VehicleRepo vehicleRepo;
    private final DriverRepo driverRepo;
    private final RouteRepo routeRepo;

    private RoutingModel routing;


    static {
        Loader.loadNativeLibraries();
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
    public void generateRoute(String companyRegistrationNumber, String city) {
        // Recuperați datele necesare
        List<Package> unassignedPackages = getUnassignedPackagesByCity(companyRegistrationNumber, city);
        if (unassignedPackages.isEmpty()) {
            throw new PackageNotFoundException("No unassigned packages found for company with registration number " + companyRegistrationNumber);
        }

        List<Vehicle> inactiveVehicles = getInactiveVehicles(companyRegistrationNumber);
        if (inactiveVehicles.isEmpty()) {
            throw new VehicleNotFoundException("No vehicles found for company with registration number " + companyRegistrationNumber);
        }

        List<Driver> availableDrivers = getAvailableDrivers(companyRegistrationNumber);
        if (availableDrivers.isEmpty()) {
            throw new DriverNotFoundException("No available drivers found for the company");
        }

        int numPackages = unassignedPackages.size();
        int numVehicles = inactiveVehicles.size();
        int numNodes = numPackages + 1; // Include punctul de start

        RoutingIndexManager manager = new RoutingIndexManager(numNodes, numVehicles, 0);
        this.routing = new RoutingModel(manager);

        City startCity = getCityByName(city);

        // Calculați matricea de distanțe
        long[][] distanceMatrix = new long[numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if (i != j) {
                    double distance;
                    if (i == 0) {
                        distance = calculateDistance(startCity.getLat(), startCity.getLng(),
                                unassignedPackages.get(j - 1).getShipment().getDestinationAddress().getCityDetails().getLat(),
                                unassignedPackages.get(j - 1).getShipment().getDestinationAddress().getCityDetails().getLng());
                    } else if (j == 0) {
                        distance = calculateDistance(unassignedPackages.get(i - 1).getShipment().getDestinationAddress().getCityDetails().getLat(),
                                unassignedPackages.get(i - 1).getShipment().getDestinationAddress().getCityDetails().getLng(),
                                startCity.getLat(), startCity.getLng());
                    } else {
                        distance = calculateDistance(unassignedPackages.get(i - 1).getShipment().getDestinationAddress().getCityDetails().getLat(),
                                unassignedPackages.get(i - 1).getShipment().getDestinationAddress().getCityDetails().getLng(),
                                unassignedPackages.get(j - 1).getShipment().getDestinationAddress().getCityDetails().getLat(),
                                unassignedPackages.get(j - 1).getShipment().getDestinationAddress().getCityDetails().getLng());
                    }
                    distanceMatrix[i][j] = (long) (distance * 1000);
                } else {
                    distanceMatrix[i][j] = 0;
                }
            }
        }

        final int transitCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
            int fromNode = manager.indexToNode((int) fromIndex);
            int toNode = manager.indexToNode((int) toIndex);
            return distanceMatrix[fromNode][toNode];
        });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        final int[] demands = new int[numPackages];
        for (int i = 0; i < numPackages; i++) {
            Package pack = unassignedPackages.get(i);
            demands[i] = (int) (pack.getVolume());
        }
        final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode((int) fromIndex);
            return fromNode == 0 ? 0 : demands[fromNode - 1];
        });

        final long[] vehicleCapacities = new long[numVehicles];
        for (int i = 0; i < numVehicles; i++) {
            vehicleCapacities[i] = (long) inactiveVehicles.get(i).getVolume();
        }

        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
        routing.setFixedCostOfAllVehicles(0);

        routing.addDimensionWithVehicleCapacity(
                demandCallbackIndex,
                0,
                vehicleCapacities,
                true,
                "Capacity"
        );

        routing.addDimension(
                transitCallbackIndex,
                0,
                1800000,
                true,
                "Distance"
        );
        RoutingDimension distanceDimension = routing.getMutableDimension("Distance");
        distanceDimension.setGlobalSpanCostCoefficient(1);

        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .mergeLocalSearchOperators(RoutingSearchParameters.LocalSearchNeighborhoodOperators.newBuilder()
                                .setUseCross(OptionalBoolean.BOOL_TRUE)
                                .setUsePathLns(OptionalBoolean.BOOL_TRUE)
                                .setUseMakeActive(OptionalBoolean.BOOL_TRUE)
                                .setUseRelocate(OptionalBoolean.BOOL_TRUE)
                                .setUseExchange(OptionalBoolean.BOOL_TRUE)
                                .build())
                        .setLsOperatorNeighborsRatio(0.2)
                        .setTimeLimit(Duration.newBuilder().setSeconds(6).build())
                        .build();

        Assignment solution = routing.solveWithParameters(searchParameters);

        if (solution != null) {
            double totalDistance = 0;

            for (int i = 0; i < numVehicles; i++) {
                System.out.println("Route for Vehicle " + i + ":");
                long index = routing.start(i);
                List<Long> routeNodes = new ArrayList<>();
                double vehicleDistance = 0;

                while (!routing.isEnd(index)) {
                    routeNodes.add(index);
                    long nextIndex = solution.value(routing.nextVar(index));
                    int fromNode = manager.indexToNode((int) index);
                    int toNode = manager.indexToNode((int) nextIndex);

                    double distance = distanceMatrix[fromNode][toNode];
                    vehicleDistance += distance;

                    if (fromNode == 0) {
                        System.out.print("Start -> ");
                    } else {
                        System.out.print("Location " + fromNode + " -> ");
                    }
                    index = nextIndex;
                }
                System.out.println("End");
                System.out.println("Total distance for Vehicle " + i + ": " + String.format("%.2f", vehicleDistance / 1000) + " KM");
                totalDistance += vehicleDistance;
            }
            double totalDistanceInKm = totalDistance / 1000;
            System.out.println("Total distance for all vehicles: " + String.format("%.2f", totalDistanceInKm) + " KM");

            saveRoutes(solution, manager, unassignedPackages, inactiveVehicles, availableDrivers, city , distanceMatrix);
        } else {
            throw new RuntimeException("No solution found for the vehicle routing problem");
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void saveRoutes(Assignment solution, RoutingIndexManager manager, List<Package> unassignedPackages, List<Vehicle> inactiveVehicles, List<Driver> availableDrivers, String city , long[][] distanceMatrix) {
        int numVehicles = inactiveVehicles.size();
        Set<String> existingAWBs = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < numVehicles; i++) {
            Vehicle vehicle = inactiveVehicles.get(i);
            Driver driver = availableDrivers.isEmpty() ? null : availableDrivers.get(random.nextInt(availableDrivers.size()));

            Route route = new Route();
            route.setVehicle(vehicle);
            route.setCompany(vehicle.getCompany());
            route.setDepartureDate(LocalDateTime.now());
            route.setStartPoint(city);
            String endPoint = city;
            LocalDateTime arrivalTime = LocalDateTime.now().plusHours(1);
            int deliverySequence = 1;
            long index = routing.start(i);
            List<Order> orders = new ArrayList<>();
            double routeDistance = 0;

            while (!routing.isEnd(index)) {
                int fromNode = manager.indexToNode((int) index);
                long nextIndex = solution.value(routing.nextVar(index));
                int toNode = manager.indexToNode((int) nextIndex);
                Package pack = fromNode == 0 ? null : unassignedPackages.get(fromNode - 1);

                if (pack != null && !existingAWBs.contains(pack.getAwb())) {
                    Order order = Convertor.convertPackageToOrder(pack);
                    order.setDeliverySequence(deliverySequence++);
                    orders.add(order);
                    existingAWBs.add(pack.getAwb());
                    pack.getShipment().setEstimatedDeliveryDate(arrivalTime);
                    arrivalTime = arrivalTime.plusMinutes(10);
                    endPoint = pack.getShipment().getDestinationAddress().getCityDetails().getCity();

                }

                double distance = distanceMatrix[fromNode][toNode];
                routeDistance += distance;
                index = nextIndex;
            }

            route.setTotalDistance(Double.parseDouble(String.format("%.2f", routeDistance/1000)));
            route.setEndPoint(endPoint);
            if (!orders.isEmpty()) {




                route.setDriver(driver);

                route.setArrivalTime(arrivalTime);

                for (Order order : orders) {
                    route.addOrder(order);
                }

                routeRepo.saveAndFlush(route);
                System.out.println("Route generated: " + route);

                for (Order order : orders) {
                    Package p = unassignedPackages.stream()
                            .filter(pkg -> pkg.getAwb().equals(order.getAwb()))
                            .findFirst()
                            .orElse(null);
                    if (p != null) {
                        p.setStatus(PackageStatus.ASSIGNED);
                        packageRepo.saveAndFlush(p);
                    }

                    if (driver == null) {
                        throw new DriverNotFoundException("No available drivers found for the company");
                    }

                    driver.increaseSalaryByKilometers(routeDistance/1000);
                    driver.setAvailable(false);
                    driverRepo.saveAndFlush(driver);


                    vehicle.increaseKilometers(routeDistance/1000);
                    vehicle.setActive(true);
                    vehicleRepo.saveAndFlush(vehicle);
                }
            } else {
                System.out.println("No orders to save for Vehicle " + i);
            }
        }
    }


    private List<Package> getUnassignedPackagesByCity(String companyRegistrationNumber, String city) {
        return packageRepo.getAllUnassignedPackagesByCityAndCompany(companyRegistrationNumber, city)
                .orElseThrow(() -> new PackageNotFoundException("No unassigned packages found for company with registration number " + companyRegistrationNumber));
    }

    private List<Vehicle> getInactiveVehicles(String companyRegistrationNumber) {
        return vehicleRepo.getInactiveVehiclesByCompanyRegistrationNumber(companyRegistrationNumber)
                .orElseThrow(() -> new VehicleNotFoundException("No vehicles found for company with registration number " + companyRegistrationNumber));
    }

    private List<Driver> getAvailableDrivers(String companyRegistrationNumber) {
        List<Driver> drivers = driverRepo.findAllByCompanyRegistrationNumberAndIsAvailableTrue(companyRegistrationNumber).orElse(new ArrayList<>());
        if (drivers.isEmpty()) {
            throw new DriverNotFoundException("No available drivers found for the company");
        }
        return drivers;
    }
}