package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.orders.models.Order;
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
import org.jetbrains.annotations.NotNull;
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

    public RouteServiceCommandImpl(PackageRepo packageRepo, VehicleRepo vehicleRepo, DriverRepo driverRepo, OrderRepo orderRepo, RouteRepo routeRepo) {
        this.packageRepo = packageRepo;
        this.vehicleRepo = vehicleRepo;
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
        this.routeRepo = routeRepo;
    }

    @Override
    @Transactional
    public void generateRoute(String companyRegistrationNumber) {
        Optional<List<Package>> packages = packageRepo.getAllUnassignedPackages(companyRegistrationNumber);

        if (!packages.isPresent()) {
            throw new PackageNotFoundException("No unassigned packages found for company with registration number " + companyRegistrationNumber);
        }
        Optional<List<Vehicle>> vehicles = vehicleRepo.getInactiveVehiclesByCompanyRegistrationNumber(companyRegistrationNumber);
        if (!vehicles.isPresent()) {
            throw new VehicleNotFoundException("No vehicles found for company with registration number " + companyRegistrationNumber);
        }

        double totalWidth = 0;
        double totalHeight = 0;
        double totalLength = 0;
        double totalWeight = 0;
        for (Package p : packages.get()) {
            totalWidth += p.getWidth();
            totalHeight += p.getHeight();
            totalLength += p.getLength();
            totalWeight += p.getWeight();
        }

        List<Vehicle> suitableVehicles = getVehicles(vehicles, totalWidth, totalHeight, totalLength, totalWeight);

        if (suitableVehicles.isEmpty()) {
            System.out.println("No suitable vehicles found for the packages");
//            todo : try to split the packages and assign them to multiple vehicles


        } else {
            createRoute(companyRegistrationNumber, suitableVehicles, totalWidth, totalHeight, totalLength, totalWeight, packages);
        }

    }

    private void createRoute(String companyRegistrationNumber, List<Vehicle> suitableVehicles, double totalWidth, double totalHeight, double totalLength, double totalWeight, Optional<List<Package>> packages) {
        System.out.println("Selected vehicle is enough to carry all packages");
        Vehicle selectedVehicle = getSelectedVehicle(suitableVehicles, totalWidth, totalHeight, totalLength, totalWeight);
        Driver driver = driverRepo.findAllByCompanyRegistrationNumberAndIsAvailableTrue(companyRegistrationNumber).get().get(0);
        System.out.println("Selected vehicle : " + selectedVehicle.getRegistrationNumber());
        System.out.println("Selected driver : " + driver);
        assignedOrdersToRoute(selectedVehicle, driver, packages);
    }

    @NotNull
    private static Vehicle getSelectedVehicle(List<Vehicle> suitableVehicles, double totalWidth, double totalHeight, double totalLength, double totalWeight) {
        Vehicle selectedVehicle = new Vehicle();
        double minDifference = Double.MAX_VALUE;

        for (Vehicle v : suitableVehicles) {
            double widthDifference = Math.abs(v.getWidth() - totalWidth);
            double heightDifference = Math.abs(v.getHeight() - totalHeight);
            double lengthDifference = Math.abs(v.getLength() - totalLength);
            double weightDifference = Math.abs(v.getWeight() - totalWeight);

            double totalDifference = widthDifference + heightDifference + lengthDifference + weightDifference;

            if (totalDifference < minDifference) {
                minDifference = totalDifference;
                selectedVehicle = v;
            }
        }
        return selectedVehicle;
    }

    @NotNull
    private static List<Vehicle> getVehicles(Optional<List<Vehicle>> vehicles, double totalWidth, double totalHeight, double totalLength, double totalWeight) {
        List<Vehicle> suitableVehicles = new ArrayList<>();
        for (Vehicle v : vehicles.get()) {
            if (v.getWidth() >= totalWidth &&
                    v.getHeight() >= totalHeight &&
                    v.getLength() >= totalLength &&
                    v.getWeight() >= totalWeight) {
                suitableVehicles.add(v);
            }
        }
        return suitableVehicles;
    }

    private void assignedOrdersToRoute(Vehicle selectedVehicle, Driver driver, Optional<List<Package>> packages) {
        Route route = new Route();
        route.setVehicle(selectedVehicle);
        route.setDriver(driver);
        route.setArrivalTime(LocalDateTime.now().plusDays(3));
        route.setDepartureDate(LocalDateTime.now().plusHours(3));
        for (Package p : packages.get()) {
            Order order = Convertor.convertPackageToOrder(p);
            route.addOrder(order);
        }
        routeRepo.saveAndFlush(route);
    }
}
