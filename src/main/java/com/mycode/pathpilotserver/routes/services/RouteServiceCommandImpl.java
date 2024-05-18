package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteServiceCommandImpl implements RouteServiceCommand {

    private final PackageRepo packageRepo;
    private final VehicleRepo vehicleRepo;

    public RouteServiceCommandImpl(PackageRepo packageRepo, VehicleRepo vehicleRepo) {
        this.packageRepo = packageRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    @Transactional
    public void generateRoute(String companyRegistrationNumber) {
        //  todo : Get all unassigned packages from a company
        Optional<List<Package>> packages = packageRepo.getAllUnassignedPackages(companyRegistrationNumber);

        if (!packages.isPresent()) {
            throw new PackageNotFoundException("No unassigned packages found for company with registration number " + companyRegistrationNumber);
        }
//       todo : Get all active vehicles from a company
        Optional<List<Vehicle>> vehicles = vehicleRepo.getInactiveVehiclesByCompanyRegistrationNumber(companyRegistrationNumber);
        if (!vehicles.isPresent()) {
            throw new VehicleNotFoundException("No vehicles found for company with registration number " + companyRegistrationNumber);
        }

//      todo : find the width , height , length and weight of all packages
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
        System.out.println("Total width: " + totalWidth);
        System.out.println("Total height: " + totalHeight);
        System.out.println("Total length: " + totalLength);
        System.out.println("Total weight: " + totalWeight);


//       todo : Get vehicle or multiple vehicle base on packages dimensions

        List<Vehicle> suitableVehicles = new ArrayList<>();
        for (Vehicle v : vehicles.get()) {
            if (v.getWidth() >= totalWidth &&
                    v.getHeight() >= totalHeight &&
                    v.getLength() >= totalLength &&
                    v.getWeight() >= totalWeight) {
                suitableVehicles.add(v);
            }
        }

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


        System.out.println("Selected vehicle: " + selectedVehicle.getRegistrationNumber());
//       todo: find for each vechile one driver and assign the vehicle to the driver





    }
}
