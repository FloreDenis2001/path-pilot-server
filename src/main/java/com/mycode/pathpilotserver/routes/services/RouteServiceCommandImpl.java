package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.graph.models.CountryGraphBuilder;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RouteServiceCommandImpl implements RouteServiceCommand {

    private final PackageRepo packageRepo;
    private final VehicleRepo vehicleRepo;
    private final DriverRepo driverRepo;
    private final OrderRepo orderRepo;
    private final RouteRepo routeRepo;

    private final CountryGraphBuilder countryGraphBuilder;

    public RouteServiceCommandImpl(CountryGraphBuilder countryGraphBuilder,
                                   PackageRepo packageRepo,
                                   VehicleRepo vehicleRepo,
                                   DriverRepo driverRepo,
                                   OrderRepo orderRepo,
                                   RouteRepo routeRepo) {
        this.packageRepo = packageRepo;
        this.vehicleRepo = vehicleRepo;
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
        this.routeRepo = routeRepo;
        this.countryGraphBuilder = countryGraphBuilder;
    }

    @Override
    @Transactional
    public void generateRoute(String companyRegistrationNumber){
        Graph<String, DefaultWeightedEdge> graphCities = countryGraphBuilder.getGraph();

        List<Package> packageList = getUnassignedPackages(companyRegistrationNumber);
        List<Vehicle> suitableVehicles = getInactiveVehicles(companyRegistrationNumber);
        List<Driver> drivers = getAvailableDrivers(companyRegistrationNumber);

        Map<Vehicle, List<Package>> vehiclePackageMap = findOptimalRoutes(packageList, suitableVehicles);

        assignPackagesToRoutes(vehiclePackageMap, drivers, graphCities);
    }



    private void assignPackagesToRoutes(Map<Vehicle, List<Package>> vehiclePackageMap, List<Driver> drivers, Graph<String, DefaultWeightedEdge> graphCities) {
        int driverIndex = 0;

        for (Map.Entry<Vehicle, List<Package>> entry : vehiclePackageMap.entrySet()) {
            if (driverIndex >= drivers.size()) {
                throw new RuntimeException("Not enough drivers available for all routes");
            }

            Vehicle vehicle = entry.getKey();
            List<Package> routePackages = entry.getValue();
            Driver driver = drivers.get(driverIndex);

            assignPackagesToRoute(routePackages, driver, vehicle, graphCities);
            driverIndex++;
        }
    }

    private void assignPackagesToRoute(List<Package> routePackages, Driver driver, Vehicle selectedVehicle, Graph<String, DefaultWeightedEdge> graphCities) {
        Set<String> packageCities = new HashSet<>();
        for (Package p : routePackages) {
            packageCities.add(p.getShipment().getDestinationAddress().getCity());
        }

        List<String> cities = new ArrayList<>(packageCities);
        String startCity = "Bucureşti";

        List<String> optimalRoute = generateOptimalRoute(startCity, cities, graphCities);

        Route route = new Route();
        route.setVehicle(selectedVehicle);
        route.setDriver(driver);
        route.setArrivalTime(LocalDateTime.now().plusDays(3));
        route.setDepartureDate(LocalDateTime.now().plusHours(3));

        for (String city : optimalRoute) {
            for (Package p : routePackages) {
                if (p.getShipment().getDestinationAddress().getCity().equals(city)) {
                    route.addOrder(Convertor.convertPackageToOrder(p));
                    System.out.println("Package " + p.toString());
                }
            }
        }
        System.out.println("Route generated: " + route.toString());
    }

    private Map<Vehicle, List<Package>> findOptimalRoutes(List<Package> packages, List<Vehicle> vehicles) {
        Map<Vehicle, List<Package>> vehiclePackageMap = new HashMap<>();
        int maxVehicles = vehicles.size();

        for (int numVehicles = 1; numVehicles <= maxVehicles; numVehicles++) {
            List<List<Package>> currentRoutes = new ArrayList<>();
            findRoutes(packages, vehicles, currentRoutes, vehiclePackageMap, 0, numVehicles);
            if (!currentRoutes.isEmpty()) {
                for (int i = 0; i < currentRoutes.size(); i++) {
                    vehiclePackageMap.put(vehicles.get(i), currentRoutes.get(i));
                }
                break;
            }
        }

        return vehiclePackageMap;
    }

    private void findRoutes(List<Package> packages, List<Vehicle> vehicles, List<List<Package>> currentRoutes, Map<Vehicle, List<Package>> vehiclePackageMap, int start, int numVehicles) {
        if (numVehicles == 0) {
            if (packages.isEmpty()) {
                for (int i = 0; i < currentRoutes.size(); i++) {
                    vehiclePackageMap.put(vehicles.get(i), new ArrayList<>(currentRoutes.get(i)));
                }
            }
            return;
        }

        for (int i = start; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            List<Package> vehiclePackages = new ArrayList<>();
            List<Package> remainingPackages = new ArrayList<>(packages);

            double currentWidth = 0;
            double currentHeight = 0;
            double currentLength = 0;
            double currentWeight = 0;

            for (Package p : packages) {
                if (currentWidth + p.getWidth() <= vehicle.getWidth() &&
                        currentHeight + p.getHeight() <= vehicle.getHeight() &&
                        currentLength + p.getLength() <= vehicle.getLength() &&
                        currentWeight + p.getWeight() <= vehicle.getWeight()) {
                    vehiclePackages.add(p);
                    currentWidth += p.getWidth();
                    currentHeight += p.getHeight();
                    currentLength += p.getLength();
                    currentWeight += p.getWeight();
                    remainingPackages.remove(p);
                }
            }

            currentRoutes.add(vehiclePackages);
            findRoutes(remainingPackages, vehicles, currentRoutes, vehiclePackageMap, i + 1, numVehicles - 1);
            currentRoutes.remove(currentRoutes.size() - 1);
        }
    }

    private List<String> generateOptimalRoute(String startCity, List<String> cities, Graph<String, DefaultWeightedEdge> graphCities) {
        SimpleWeightedGraph<String, DefaultWeightedEdge> tspGraph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        tspGraph.addVertex(startCity);
        for (String city : cities) {
            tspGraph.addVertex(city);
        }

        for (String city1 : tspGraph.vertexSet()) {
            for (String city2 : tspGraph.vertexSet()) {
                if (!city1.equals(city2)) {
                    DefaultWeightedEdge edge = graphCities.getEdge(city1, city2);
                    if (edge != null) {
                        tspGraph.addEdge(city1, city2, edge);
                        tspGraph.setEdgeWeight(edge, graphCities.getEdgeWeight(edge));
                    }
                }
            }
        }

        HeldKarpTSP<String, DefaultWeightedEdge> tspSolver = new HeldKarpTSP<>();
        GraphPath<String, DefaultWeightedEdge> tour = tspSolver.getTour(tspGraph);
        if (tour != null) {
            return tour.getVertexList();
        } else {
            return nearestNeighborTSP(startCity, cities, graphCities);
        }
    }

    private List<String> nearestNeighborTSP(String startCity, List<String> cities, Graph<String, DefaultWeightedEdge> graphCities) {
        List<String> tour = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        String currentCity = startCity;
        tour.add(currentCity);
        visited.add(currentCity);

        while (visited.size() < cities.size() + 1) {
            String nearestCity = null;
            double minDistance = Double.MAX_VALUE;

            for (String city : cities) {
                if (!visited.contains(city)) {
                    double distance = graphCities.getEdgeWeight(graphCities.getEdge(currentCity, city));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCity = city;
                    }
                }
            }

            if (nearestCity != null) {
                tour.add(nearestCity);
                visited.add(nearestCity);
                currentCity = nearestCity;
            } else {
                break;
            }
        }

        return tour;
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
