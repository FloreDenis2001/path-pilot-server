package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;
import com.mycode.pathpilotserver.driver.dto.DriverUpdateRequest;
import com.mycode.pathpilotserver.driver.exceptions.DriverAlreadyExistException;
import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mycode.pathpilotserver.city.utils.Utils.getCityByName;

@Service
@Transactional
public class DriverCommandServiceImpl implements DriverCommandService {

    private final DriverRepo driverRepo;
    private final UserRepo userRepo;
    private final CompanyRepo companyRepo;

    public DriverCommandServiceImpl(DriverRepo driverRepo, UserRepo userRepo, CompanyRepo companyRepo) {
        this.driverRepo = driverRepo;
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public void create(DriverCreateRequest driverCreateRequest) {
        validateCreateRequest(driverCreateRequest);

        Optional<Driver> user = driverRepo.findByLicenseNumber(driverCreateRequest.licenseNumber());
        Optional<Company> company = companyRepo.findByRegistrationNumber(driverCreateRequest.companyRegistrationNumber());
        if (user.isPresent()) {
            throw new DriverAlreadyExistException("Driver with license number: " + driverCreateRequest.licenseNumber() + " already exists");
        }

        if (company.isEmpty()) {
            throw new CompanyNotFoundException("Company with registration number: " + driverCreateRequest.companyRegistrationNumber() + " doesn't exist!");
        }

        Driver newDriver = getDriver(driverCreateRequest, company);
        driverRepo.saveAndFlush(newDriver);
    }

    @NotNull
    private Driver getDriver(DriverCreateRequest driverCreateRequest, Optional<Company> company) {
        if (company.isEmpty()) {
            throw new CompanyNotFoundException("Company with registration number: " + driverCreateRequest.companyRegistrationNumber() + " doesn't exist!");
        }

        Driver newDriver = new Driver();
        newDriver.setLicenseNumber(driverCreateRequest.licenseNumber());
        newDriver.setPhone(driverCreateRequest.phone());
        newDriver.setUsername(driverCreateRequest.username());
        newDriver.setPassword(driverCreateRequest.password());
        newDriver.setEmail(driverCreateRequest.email());
        newDriver.setFirstName(driverCreateRequest.firstName());
        newDriver.setLastName(driverCreateRequest.lastName());
        newDriver.setExperience(0);
        newDriver.setRating(0);
        newDriver.setSalary(0);
        newDriver.setRole(UserRole.DRIVER);
        newDriver.setCompany(company.get());

        City city = getCityByName(driverCreateRequest.address().city());
        Address fullAddress = buildAddress(city, driverCreateRequest.address());
        newDriver.setAddress(fullAddress);

        newDriver.setAvailable(true);
        return newDriver;
    }

    private Address buildAddress(City city, AddressDTO addressDTO) {
        return Address.builder()
                .cityDetails(city)
                .street(addressDTO.street())
                .postalCode(addressDTO.postalCode())
                .streetNumber(addressDTO.streetNumber())
                .build();
    }

    @Override
    public void update(DriverUpdateRequest driverUpdateRequest) {
        validateUpdateRequest(driverUpdateRequest);

        Optional<Company> company = companyRepo.findByRegistrationNumber(driverUpdateRequest.companyRegistrationNumber());
        if (company.isEmpty()) {
            throw new CompanyNotFoundException("Company with registration number: " + driverUpdateRequest.companyRegistrationNumber() + " doesn't exist!");
        }

        Optional<Driver> driver = driverRepo.findByLicenseNumberAndCompany(driverUpdateRequest.licenseNumber(), company.get());
        if (driver.isPresent()) {
            Driver existingDriver = getDriver(driverUpdateRequest, driver);
            driverRepo.saveAndFlush(existingDriver);
        } else {
            throw new DriverNotFoundException("Driver with license number: " + driverUpdateRequest.licenseNumber() + " not found");
        }
    }

    @NotNull
    private static Driver getDriver(DriverUpdateRequest driverUpdateRequest, Optional<Driver> driver) {
        Driver existingDriver = driver.get();
        existingDriver.setLicenseNumber(driverUpdateRequest.licenseNumber());
        existingDriver.setPhone(driverUpdateRequest.phone());
        existingDriver.setUsername(driverUpdateRequest.username());
        existingDriver.setEmail(driverUpdateRequest.email());
        existingDriver.setFirstName(driverUpdateRequest.firstName());
        existingDriver.setLastName(driverUpdateRequest.lastName());
        existingDriver.setExperience(driverUpdateRequest.experience());
        existingDriver.setRating(driverUpdateRequest.rating());
        existingDriver.setSalary(driverUpdateRequest.salary());
        existingDriver.setAvailable(driverUpdateRequest.isAvailable());
        return existingDriver;
    }

    @Override
    public void removeByLicenseNumber(String email, String licenseNumber, String companyRegistrationNumber) {
        Optional<Company> company = companyRepo.findByRegistrationNumber(companyRegistrationNumber);
        if (company.isEmpty()) {
            throw new CompanyNotFoundException("Company with registration number: " + companyRegistrationNumber + " doesn't exist!");
        }

        Optional<Driver> driver = driverRepo.findByLicenseNumberAndCompany(licenseNumber, company.get());
        if (driver.isEmpty()) {
            throw new DriverNotFoundException("Driver with license number: " + licenseNumber + " not found");
        }

        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email: " + email + " not found");
        }

        if (user.get().getRole() == UserRole.DRIVER) {
            driver.get().getRoutes().forEach(route -> route.setDriver(null));
            driverRepo.delete(driver.get());
        } else {
            throw new UserNotFoundException("User with email: " + email + " doesn't have permission to delete driver");
        }
    }

    private void validateCreateRequest(DriverCreateRequest request) {
        if (request.licenseNumber() == null || request.licenseNumber().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be null or empty");
        }
        if (request.username() == null || request.username().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (request.email() == null || request.email().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (request.phone() == null || request.phone().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (request.address() == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        if (request.address().city() == null || request.address().city().isEmpty()) {
            throw new IllegalArgumentException("City in address cannot be null or empty");
        }
        if (request.companyRegistrationNumber() == null || request.companyRegistrationNumber().isEmpty()) {
            throw new IllegalArgumentException("Company registration number cannot be null or empty");
        }
    }

    private void validateUpdateRequest(DriverUpdateRequest request) {
        if (request.licenseNumber() == null || request.licenseNumber().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be null or empty");
        }
        if (request.username() == null || request.username().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (request.email() == null || request.email().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (request.phone() == null || request.phone().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (request.companyRegistrationNumber() == null || request.companyRegistrationNumber().isEmpty()) {
            throw new IllegalArgumentException("Company registration number cannot be null or empty");
        }
    }
}
