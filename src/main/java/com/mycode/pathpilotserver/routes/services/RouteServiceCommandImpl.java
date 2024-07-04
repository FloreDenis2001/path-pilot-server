package com.mycode.pathpilotserver.routes.services;


import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;

import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceCommandImpl implements RouteServiceCommand {

    private final PackageRepo packageRepo;
    private final VehicleRepo vehicleRepo;
    private final DriverRepo driverRepo;
    private final RouteRepo routeRepo;




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