package com.mycode.pathpilotserver.routes.services;


import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;

import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
}