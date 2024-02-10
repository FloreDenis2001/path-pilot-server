package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;

import java.util.Optional;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;

public class DriverServiceQuerryImpl implements DriverServiceQuerry{

    private final DriverRepo driverRepo;

    public DriverServiceQuerryImpl(DriverRepo driverRepo) {
        this.driverRepo = driverRepo;
    }

    @Override
    public Optional<Driver> findByName(String name) {
        Optional<Driver> driver = driverRepo.findByName(name);
        if (driver.isPresent()) {
            return driver;}
        else {
            throw new DriverNotFoundException("Driver with name: " + name + " not found");
        }
    }


}
