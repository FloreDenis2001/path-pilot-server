package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;
import com.mycode.pathpilotserver.driver.exceptions.DriverAlreadyExistException;
import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class DriverCommandServiceImpl implements DriverCommandService{

    private final DriverRepo driverRepo;
    private final UserRepo userRepo;

    public DriverCommandServiceImpl(DriverRepo driverRepo, UserRepo userRepo) {
        this.driverRepo = driverRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void create(DriverCreateRequest driverCreateRequest) {
        Optional<User> user = userRepo.findByEmail(driverCreateRequest.email());

        if(user.isPresent()) {
            throw new DriverAlreadyExistException("Driver with license number: " + driverCreateRequest.licenseNumber() + " already exist");
        }else {
            Driver newDriver= new Driver();
            newDriver.setLicenseNumber(driverCreateRequest.licenseNumber());
            newDriver.setPhone(driverCreateRequest.phone());
            newDriver.setUsername(driverCreateRequest.username());
            newDriver.setPassword(driverCreateRequest.password());
            newDriver.setEmail(driverCreateRequest.email());
            newDriver.setFirstName(driverCreateRequest.firstName());
            newDriver.setLastName(driverCreateRequest.lastName());
            newDriver.setExperience(driverCreateRequest.experience());
            newDriver.setRating(driverCreateRequest.rating());
            newDriver.setSalary(driverCreateRequest.salary());
            newDriver.setRole(UserRole.DRIVER);
            newDriver.setAvailable(true);
            driverRepo.saveAndFlush(newDriver);
        }

    }


}
