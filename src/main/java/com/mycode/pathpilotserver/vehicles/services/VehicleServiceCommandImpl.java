package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
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
    private final CompanyRepo companyRepo;

    public VehicleServiceCommandImpl(VehicleRepo vehicleRepo, CompanyRepo companyRepo) {
        this.vehicleRepo = vehicleRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public void create(CreateVehicleRequest createVehicleRequest) {
        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(createVehicleRequest.registrationNumber());
        Optional<Company> company = companyRepo.findByRegistrationNumber(createVehicleRequest.companyRegistrationNumber());

        if (vehicle.isPresent()) {
            throw new VehicleAlreadyExistException("Vehicle with registration number " + createVehicleRequest.registrationNumber() + " already exist");
        }

        if (company.isEmpty()) {
            throw new CompanyNotFoundException("Company with registration number " + createVehicleRequest.companyRegistrationNumber() + " not found");
        }

        Vehicle vehicleCreated = createVehicle(createVehicleRequest, company.get());
        vehicleRepo.save(vehicleCreated);
    }

    @Override
    public void update(UpdatedVehicleRequest updatedVehicleRequest) {
        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(updatedVehicleRequest.registrationNumber());
        if (vehicle.isPresent()) {
            Vehicle existingVehicle = vehicle.get();
            updateVehicleAttributes(existingVehicle, updatedVehicleRequest);
            vehicleRepo.save(existingVehicle);
        } else {
            throw new VehicleNotFoundException("Vehicle with registration number " + updatedVehicleRequest.registrationNumber() + " not found");
        }
    }

    private void updateVehicleAttributes(Vehicle vehicle, UpdatedVehicleRequest updatedVehicleRequest) {
        vehicle.setRegistrationNumber(updatedVehicleRequest.registrationNumber());
        vehicle.setFuelType(updatedVehicleRequest.fuelType());
        vehicle.setMake(updatedVehicleRequest.make());
        vehicle.setModel(updatedVehicleRequest.model());
        vehicle.setYear(updatedVehicleRequest.year());
        vehicle.setKm(updatedVehicleRequest.km());
        vehicle.setFuelCapacity(updatedVehicleRequest.fuelCapacity());
        vehicle.setFuelConsumption(updatedVehicleRequest.fuelConsumption());
        vehicle.setLastService(updatedVehicleRequest.lastService());
        vehicle.setNextService(updatedVehicleRequest.nextService());
        vehicle.setCapacity(updatedVehicleRequest.capacity());
        vehicle.setWidth(updatedVehicleRequest.width());
        vehicle.setHeight(updatedVehicleRequest.height());
        vehicle.setLength(updatedVehicleRequest.length());
        vehicle.setWeight(updatedVehicleRequest.weight());
        vehicle.setActive(updatedVehicleRequest.active());
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


    private static Vehicle createVehicle(CreateVehicleRequest createVehicleRequest, Company company) {
        return Vehicle.builder().registrationNumber(createVehicleRequest.registrationNumber()).fuelType(createVehicleRequest.fuelType()).make(createVehicleRequest.make()).model(createVehicleRequest.model()).year(createVehicleRequest.year()).km(createVehicleRequest.km()).fuelCapacity(createVehicleRequest.fuelCapacity()).fuelConsumption(createVehicleRequest.fuelConsumption()).lastService(createVehicleRequest.lastService()).nextService(createVehicleRequest.nextService()).capacity(createVehicleRequest.capacity()).weight(createVehicleRequest.weight()).width(createVehicleRequest.width()).height(createVehicleRequest.height()).length(createVehicleRequest.length()).isActive(false).company(company).build();
    }


}
