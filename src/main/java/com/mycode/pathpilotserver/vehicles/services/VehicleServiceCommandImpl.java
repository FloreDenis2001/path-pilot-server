package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.dto.CreateVehicleRequest;
import com.mycode.pathpilotserver.vehicles.dto.UpdatedVehicleRequest;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleAlreadyExistException;
import com.mycode.pathpilotserver.vehicles.exceptions.VehicleNotFoundException;
import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            throw new VehicleAlreadyExistException("Vehicle with registration number " + createVehicleRequest.registrationNumber() + " already exist");
        } else {
            Vehicle vehicleCreated = createVehicle(createVehicleRequest);
            vehicleRepo.save(vehicleCreated);
        }
    }

    @Override
    public void update(UpdatedVehicleRequest updatedVehicleRequest) {
        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(updatedVehicleRequest.registrationNumber());
        if (vehicle.isPresent()) {
            vehicle.get().setRegistrationNumber(updatedVehicleRequest.registrationNumber());
            vehicle.get().setFuelType(updatedVehicleRequest.fuelType());
            vehicle.get().setMake(updatedVehicleRequest.make());
            vehicle.get().setModel(updatedVehicleRequest.model());
            vehicle.get().setYear(updatedVehicleRequest.year());
            vehicle.get().setKm(updatedVehicleRequest.km());
            vehicle.get().setFuelCapacity(updatedVehicleRequest.fuelCapacity());
            vehicle.get().setFuelConsumption(updatedVehicleRequest.fuelConsumption());
            vehicle.get().setLastService(updatedVehicleRequest.lastService());
            vehicle.get().setNextService(updatedVehicleRequest.nextService());
            vehicle.get().setCapacity(updatedVehicleRequest.capacity());
            vehicle.get().setWidth(updatedVehicleRequest.width());
            vehicle.get().setHeight(updatedVehicleRequest.height());
            vehicle.get().setLength(updatedVehicleRequest.length());
            vehicle.get().setWeight(updatedVehicleRequest.weight());
            vehicle.get().setActive(updatedVehicleRequest.active());
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


    private static Vehicle createVehicle(CreateVehicleRequest createVehicleRequest) {
        return Vehicle.builder()
                .registrationNumber(createVehicleRequest.registrationNumber())
                .fuelType(createVehicleRequest.fuelType())
                .make(createVehicleRequest.make())
                .model(createVehicleRequest.model())
                .year(createVehicleRequest.year())
                .km(createVehicleRequest.km())
                .fuelCapacity(createVehicleRequest.fuelCapacity())
                .fuelConsumption(createVehicleRequest.fuelConsumption())
                .lastService(createVehicleRequest.lastService())
                .nextService(createVehicleRequest.nextService())
                .capacity(createVehicleRequest.capacity())
                .width(createVehicleRequest.width())
                .height(createVehicleRequest.height())
                .length(createVehicleRequest.length())
                .isActive(false)
                .build();
    }




}
