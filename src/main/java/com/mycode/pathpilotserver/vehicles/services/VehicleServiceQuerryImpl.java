package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
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
    public Optional<List<Vehicle>> findByType(String type) {
        Optional<List<Vehicle>> vehicle = vehicleRepo.findByType(type);
        if (vehicle.isPresent()) {
            return vehicle;
        } else {
            throw new VehicleNotFoundException("Vehicles with type " + type + " not found");
        }
    }

    @Override
    public Optional<List<Vehicle>> findByCapacity(int capacity) {
        Optional<List<Vehicle>> vehicle = vehicleRepo.findByCapacity(capacity);
        if (vehicle.isPresent()) {
            return vehicle;
        } else {
            throw new VehicleNotFoundException("Vehicles with capacity " + capacity + " not found");
        }
    }


}
