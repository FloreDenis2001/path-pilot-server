package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceQuerryImpl implements DriverQuerryService {

    private final DriverRepo driverRepo;

    public DriverServiceQuerryImpl(DriverRepo driverRepo) {
        this.driverRepo = driverRepo;
    }

    @Override
    public Optional<List<DriverDTO>> getAllDrivers() {
        List<Driver> drivers = driverRepo.findAll();
        if (drivers.isEmpty()) {
            throw new DriverNotFoundException("No drivers found");
        } else {
            return Optional.of(DriverDTO.fromList(drivers));
        }
    }

    @Override
    public Optional<List<DriverDTO>> getAllDriversByCompany(String registrationNumber) {
        Optional<List<Driver>> drivers = driverRepo.findAllByCompanyRegistrationNumber(registrationNumber);
        if (drivers.isEmpty()) {
            throw new DriverNotFoundException("No drivers found");
        } else {
            return Optional.of(DriverDTO.fromList(drivers.get()));
        }
    }

    @Override
    public Optional<Driver> findByLicenseNumber(String licenseNumber) {
        Optional<Driver> driver = driverRepo.findByLicenseNumber(licenseNumber);
        if (driver.isPresent()) {
            return driver;
        } else {
            throw new DriverNotFoundException("Driver with license number: " + licenseNumber + " not found");
        }
    }


}
