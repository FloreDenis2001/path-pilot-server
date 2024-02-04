package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;

import java.util.Optional;

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
}
