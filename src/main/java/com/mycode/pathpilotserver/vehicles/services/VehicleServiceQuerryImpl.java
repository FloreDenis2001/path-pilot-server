package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class VehicleServiceQuerryImpl implements VehicleServiceQuerry {
    private final VehicleRepo vehicleRepo;

    public VehicleServiceQuerryImpl(VehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public Optional<Vehicle> findByRegistrationNumber(String registrationNumber) {
        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(registrationNumber);
        if (vehicle.isPresent()) {
            return vehicle;
        } else {
            throw new VehicleNotFoundException("Vehicle with registration number " + registrationNumber + " not found");
        }
    }

    @Override
    public Optional<List<Vehicle>> findByFuelType(FuelType  fuelType) {
        Optional<List<Vehicle>> vehicles = vehicleRepo.findByFuelType(fuelType);
        if (vehicles.isPresent()) {
            return vehicles;
        } else {
            throw new VehicleNotFoundException("No vehicles found with fuel type " + fuelType);
        }
    }

    @Override
    public Optional<List<Vehicle>> findByCapacity(int capacity) {
        Optional<List<Vehicle>> vehicles = vehicleRepo.findByCapacity(capacity);
        if (vehicles.isPresent()) {
            return vehicles;
        } else {
            throw new VehicleNotFoundException("No vehicles found with capacity " + capacity);
        }
    }


    @Override
    public Optional<List<Vehicle>> findAll() {
        Optional<List<Vehicle>> vehicles = Optional.of(vehicleRepo.findAll());
        if (vehicles.isPresent()) {
            return vehicles;
        } else {
            throw new VehicleNotFoundException("No vehicles found");
        }
    }

    @Override
    public Optional<List<Vehicle>> getVehiclesByCompanyRegistrationNumber(String registrationNumber) {
        Optional<List<Vehicle>> vehicles = vehicleRepo.getVehiclesByCompanyRegistrationNumber(registrationNumber);
        if (vehicles.isPresent()) {
            return vehicles;
        } else {
            throw new VehicleNotFoundException("No vehicles found for company with registration number " + registrationNumber);
        }
    }


}
