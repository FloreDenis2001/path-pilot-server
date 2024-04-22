package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;
import com.mycode.pathpilotserver.driver.dto.DriverUpdateRequest;
import com.mycode.pathpilotserver.driver.exceptions.DriverAlreadyExistException;
import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.exceptions.WrongPasswordException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class DriverCommandServiceImpl implements DriverCommandService {

    private final DriverRepo driverRepo;
    private final UserRepo userRepo;

    public DriverCommandServiceImpl(DriverRepo driverRepo, UserRepo userRepo) {
        this.driverRepo = driverRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void create(DriverCreateRequest driverCreateRequest) {
        Optional<Driver> user= driverRepo.findByLicenseNumber(driverCreateRequest.licenseNumber());
        if (user.isPresent()) {
            throw new DriverAlreadyExistException("Driver with license number: " + driverCreateRequest.licenseNumber() + " already exist");
        } else {
            Driver newDriver = new Driver();
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

    @Override
    public void update(DriverUpdateRequest driverUpdateRequest) {
        Optional<Driver> driver = driverRepo.findByLicenseNumber(driverUpdateRequest.licenseNumber());
        if (driver.isPresent()) {
            driver.get().setLicenseNumber(driverUpdateRequest.licenseNumber());
            driver.get().setPhone(driverUpdateRequest.phone());
            driver.get().setUsername(driverUpdateRequest.username());
            driver.get().setEmail(driverUpdateRequest.email());
            driver.get().setFirstName(driverUpdateRequest.firstName());
            driver.get().setLastName(driverUpdateRequest.lastName());
            driver.get().setExperience(driverUpdateRequest.experience());
            driver.get().setRating(driverUpdateRequest.rating());
            driver.get().setSalary(driverUpdateRequest.salary());
            driver.get().setAvailable(driverUpdateRequest.isAvailable());
            driverRepo.saveAndFlush(driver.get());
        } else {
            throw new DriverNotFoundException("Driver with license number: " + driverUpdateRequest.licenseNumber() + " not found");
        }
    }

    @Override
    public void removeByLicenseNumber(RemoveValidationRequest removeValidationRequest, String licenseNumber) {
        Optional<Driver> driver = driverRepo.findByLicenseNumber(licenseNumber);
        Optional<User> user = userRepo.findByEmail(removeValidationRequest.email());
        if(driver.isEmpty()) {
            throw new DriverNotFoundException("Driver with license number: " + licenseNumber + " not found");
        }

        validatePassword(user.get(), removeValidationRequest.password());

        if(user.get().getRole()==UserRole.CUSTOMER || user.get().equals(driver.get())){
            driver.ifPresentOrElse(driverRepo::delete, () -> {
                throw new DriverNotFoundException("Driver not found for license number: " + licenseNumber);
            });
        }
        else{
            throw new WrongPasswordException("Invalid password for user: " + user.get().getEmail());
        }
    }

    private void validatePassword(User user, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException("Invalid password for user: " + user.getEmail());
        }
    }
}
