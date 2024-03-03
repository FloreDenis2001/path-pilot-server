package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.dto.CreateVehicleRequest;
import com.mycode.pathpilotserver.vehicles.dto.UpdatedVehicleRequest;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleAlreadyExistException;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class VehicleServiceCommandImpl implements VehicleServiceCommand {

    private final VehicleRepo vehicleRepo;

    public VehicleServiceCommandImpl(VehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public void create(CreateVehicleRequest createVehicleRequest) {

        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(createVehicleRequest.registrationNumber());
        if (vehicle.isPresent()) {
            throw new VehicleAlreadyExistException("Vehicle with registration number " + createVehicleRequest.registrationNumber() + " already exists");
        } else {
            vehicleRepo.save(Vehicle.builder().capacity(createVehicleRequest.capacity()).type(createVehicleRequest.type()).registrationNumber(createVehicleRequest.registrationNumber()).build());
        }
    }

    @Override
    public void update(UpdatedVehicleRequest updatedVehicleRequest) {
        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(updatedVehicleRequest.registrationNumber());
        if (vehicle.isPresent()) {
            vehicle.get().setType(updatedVehicleRequest.type());
            vehicle.get().setCapacity(updatedVehicleRequest.capacity());
            vehicleRepo.save(vehicle.get());
        } else {
            throw new VehicleNotFoundException("Vehicle with registration number " + updatedVehicleRequest.registrationNumber() + " not found");
        }
    }

    @Override
    public void delete(String registrationNumber) {

        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(registrationNumber);
        if (vehicle.isPresent()) {
            vehicleRepo.delete(vehicle.get());
        } else {
            throw new VehicleNotFoundException("Vehicle with registration number " + registrationNumber + " not found");
        }


    }
}
