package com.mycode.pathpilotserver.routes.services;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
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

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void generateRoute(String companyRegistrationNumber, String city) throws IOException {
        // Retrieve necessary data
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

        // Create a RoutingIndexManager for CVRP
        RoutingIndexManager manager = new RoutingIndexManager(numPackages, numVehicles, 0);
        this.routing = new RoutingModel(manager);

        // Create the distance matrix
        long[][] distanceMatrix = new long[numPackages][numPackages];
        for (int i = 0; i < numPackages; i++) {
            for (int j = 0; j < numPackages; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    double distance = calculateDistance(unassignedPackages.get(i), unassignedPackages.get(j));
                    distanceMatrix[i][j] = (long) (distance * 1000);  // Convert to meters
                }
            }
        }

        // Register transit callback to use the distance matrix
        final int transitCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
            int fromNode = manager.indexToNode((int) fromIndex);
            int toNode = manager.indexToNode((int) toIndex);
            return distanceMatrix[fromNode][toNode];
        });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Define demand callback
        final int[] demands = new int[numPackages];
        for (int i = 0; i < numPackages; i++) {
            Package pack = unassignedPackages.get(i);
            demands[i] = (int) (pack.getVolume());  // Use volume as the demand for each package
        }
        final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode((int) fromIndex);
            return demands[fromNode];
        });

        // Get vehicle capacities from the inactive vehicles
        final long[] vehicleCapacities = new long[numVehicles];
        for (int i = 0; i < numVehicles; i++) {
            vehicleCapacities[i] = (long) inactiveVehicles.get(i).getVolume();  // Get capacity from each vehicle
        }

        // Add dimension for capacity constraints
        routing.addDimensionWithVehicleCapacity(
                demandCallbackIndex,  // Demand callback index
                0,  // No slack
                vehicleCapacities,  // Vehicle capacities
                true,  // Start cumul to zero
                "Capacity"
        );

        // Removed the soft upper bound constraint for capacity
        // RoutingDimension capacityDimension = routing.getMutableDimension("Capacity");
        // for (int i = 0; i < numVehicles; i++) {
        //     capacityDimension.setCumulVarSoftUpperBound(
        //         manager.nodeToIndex(0),
        //         (long) (0.5 * vehicleCapacities[i]),  // 50% of the vehicle's capacity
        //         1000000  // Penalty cost for exceeding the 50% capacity
        //     );
        // }

        // Add distance constraint with a relaxed maximum distance
        routing.addDimension(
                transitCallbackIndex,  // Transit callback index for distance
                0,  // Slack maximum
                10000, // Increased maximum distance to 10 kilometers or more
                true,  // Start cumul to zero
                "Distance"
        );
        RoutingDimension distanceDimension = routing.getMutableDimension("Distance");
        distanceDimension.setGlobalSpanCostCoefficient(100);  // Set cost coefficient for distance dimension

        // Define search parameters
        RoutingSearchParameters searchParameters =
               main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .build();

        // Solve the problem
        Assignment solution = routing.solveWithParameters(searchParameters);

        if (solution != null) {
            for (int i = 0; i < numVehicles; i++) {
                System.out.println("Route for Vehicle " + i + ":");
                long index = routing.start(i);
                List<Integer> routeNodes = new ArrayList<>();
                while (!routing.isEnd(index)) {
                    routeNodes.add(manager.indexToNode((int) index));
                    index = solution.value(routing.nextVar(index));
                }
                routeNodes.add(manager.indexToNode((int) index));

                System.out.println(routeNodes);
            }

            saveRoutes(solution, manager, unassignedPackages, inactiveVehicles, availableDrivers);
        } else {
            throw new RuntimeException("No solution found for the vehicle routing problem");
        }
    }

    private double calculateDistance(Package p1, Package p2) {
        double lat1 = p1.getShipment().getDestinationAddress().getCityDetails().getLat();
        double lon1 = p1.getShipment().getDestinationAddress().getCityDetails().getLng();
        double lat2 = p2.getShipment().getDestinationAddress().getCityDetails().getLat();
        double lon2 = p2.getShipment().getDestinationAddress().getCityDetails().getLng();

        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;  // Convert to kilometers

        return distance;
    }



    private void saveRoutes(Assignment solution, RoutingIndexManager manager, List<Package> unassignedPackages, List<Vehicle> inactiveVehicles, List<Driver> availableDrivers) {
        int numVehicles = inactiveVehicles.size();
        Set<String> existingAWBs = new HashSet<>(); // Set pentru a urmări AWB-urile deja adăugate

        for (int i = 0; i < numVehicles; i++) {
            Vehicle vehicle = inactiveVehicles.get(i);
            Driver driver = availableDrivers.get(i);

            Route route = new Route();
            route.setVehicle(vehicle);
            route.setDriver(driver);
            route.setCompany(vehicle.getCompany());
            route.setDepartureDate(LocalDateTime.now());

            LocalDateTime arrivalTime = LocalDateTime.now().plusHours(1);

            long index = routing.start(i);
            List<Order> orders = new ArrayList<>();

            while (!routing.isEnd(index)) {
                int nodeIndex = manager.indexToNode((int) index);
                Package pack = unassignedPackages.get(nodeIndex);

                if (!existingAWBs.contains(pack.getAwb())) { // Verifică dacă AWB-ul a fost deja adăugat
                    Order order = Convertor.convertPackageToOrder(pack);
                    orders.add(order);
                    existingAWBs.add(pack.getAwb()); // Adaugă AWB-ul în set

                    pack.getShipment().setEstimatedDeliveryDate(arrivalTime);
                    arrivalTime = arrivalTime.plusMinutes(10);

                    index = solution.value(routing.nextVar(index));
                } else {
                    index = solution.value(routing.nextVar(index)); // Treci la următorul nod
                }
            }

            // Verificăm dacă ruta conține comenzi înainte de a o salva
            if (!orders.isEmpty()) {
                route.setArrivalTime(arrivalTime);
                double totalDistance = solution.objectiveValue();
                route.setTotalDistance(Double.parseDouble(String.format("%.2f", totalDistance / 1000)));

                for (Order order : orders) {
                    route.addOrder(order);
                }

                routeRepo.saveAndFlush(route);
                System.out.println("Route generated: " + route.toString());

                for (Order order : orders) {
                    Package p = unassignedPackages.stream()
                            .filter(pkg -> pkg.getAwb().equals(order.getAwb()))
                            .findFirst()
                            .orElse(null);
                    if (p != null) {
                        p.setStatus(PackageStatus.ASSIGNED);
                        packageRepo.saveAndFlush(p);
                    }
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