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
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static com.mycode.pathpilotserver.city.utils.Utils.getCityByName;

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
        if (vehicleRepo.findByRegistrationNumber(createVehicleRequest.registrationNumber()).isPresent()) {
            throw new VehicleAlreadyExistException("Vehicle with registration number " + createVehicleRequest.registrationNumber() + " already exists");
        }

        Company company = companyRepo.findByRegistrationNumber(createVehicleRequest.companyRegistrationNumber())
                .orElseThrow(() -> new CompanyNotFoundException("Company with registration number " + createVehicleRequest.companyRegistrationNumber() + " not found"));

        Vehicle vehicle = Vehicle.builder()
                .currentLocation(getCityByName(createVehicleRequest.currentLocation()))
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
                .weight(createVehicleRequest.weight())
                .width(createVehicleRequest.width())
                .height(createVehicleRequest.height())
                .length(createVehicleRequest.length())
                .isActive(false)
                .company(company)
                .build();

        vehicleRepo.save(vehicle);
    }

    @Override
    public void update(UpdatedVehicleRequest updatedVehicleRequest) {
        Vehicle vehicle = vehicleRepo.findByRegistrationNumber(updatedVehicleRequest.registrationNumber())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with registration number " + updatedVehicleRequest.registrationNumber() + " not found"));

        boolean isUpdated = false;

        updateIfNotNull(updatedVehicleRequest.fuelType(), vehicle::setFuelType);
        updateIfNotNull(updatedVehicleRequest.make(), vehicle::setMake);
        updateIfNotNull(updatedVehicleRequest.model(), vehicle::setModel);
        updateIfNotNull(updatedVehicleRequest.year(), vehicle::setYear);
        updateIfNotNull(updatedVehicleRequest.km(), vehicle::setKm);
        updateIfPositive(updatedVehicleRequest.fuelCapacity(), vehicle::setFuelCapacity);
        updateIfPositive(updatedVehicleRequest.fuelConsumption(), vehicle::setFuelConsumption);
        updateIfNotNull(updatedVehicleRequest.lastService(), vehicle::setLastService);
        updateIfNotNull(updatedVehicleRequest.nextService(), vehicle::setNextService);
        updateIfNotNull(updatedVehicleRequest.capacity(), vehicle::setCapacity);
        updateIfPositive(updatedVehicleRequest.width(), vehicle::setWidth);
        updateIfPositive(updatedVehicleRequest.height(), vehicle::setHeight);
        updateIfPositive(updatedVehicleRequest.length(), vehicle::setLength);
        updateIfPositive(updatedVehicleRequest.weight(), vehicle::setWeight);

        if (updatedVehicleRequest.active() != vehicle.isActive()) {
            vehicle.setActive(updatedVehicleRequest.active());
            isUpdated = true;
        }

        if (isUpdated) {
            vehicleRepo.save(vehicle);
        }
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void updateIfPositive(double value, DoubleConsumer setter) {
        if (value > 0) {
            setter.accept(value);
        }
    }

    @Override
    public void delete(String registrationNumber) {
        Vehicle vehicle = vehicleRepo.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with registration number " + registrationNumber + " not found"));

        vehicleRepo.delete(vehicle);
    }
}
