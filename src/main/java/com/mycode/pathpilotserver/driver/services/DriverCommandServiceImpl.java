package com.mycode.pathpilotserver.driver.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.mycode.pathpilotserver.user.exceptions.WrongPasswordException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class DriverCommandServiceImpl implements DriverCommandService {

    private final DriverRepo driverRepo;
    private final UserRepo userRepo;
    private final CompanyRepo companyRepo;
    private final ObjectMapper objectMapper ;

    public DriverCommandServiceImpl(DriverRepo driverRepo, UserRepo userRepo, CompanyRepo companyRepo, ObjectMapper objectMapper) {
        this.driverRepo = driverRepo;
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public void create(DriverCreateRequest driverCreateRequest) {
        Optional<Driver> user= driverRepo.findByLicenseNumber(driverCreateRequest.licenseNumber());
        Optional<Company> company=companyRepo.findByRegistrationNumber(driverCreateRequest.companyRegistrationNumber());
        if (user.isPresent()) {
            throw new DriverAlreadyExistException("Driver with license number: " + driverCreateRequest.licenseNumber() + " already exist");
        }

        if(company.isEmpty()){
            throw new CompanyNotFoundException("Company with registration number: "+driverCreateRequest.companyRegistrationNumber()+" dosen't exist !");
        }

        Driver newDriver = getDriver(driverCreateRequest, company);
        driverRepo.saveAndFlush(newDriver);
    }

    @NotNull
    private  Driver getDriver(DriverCreateRequest driverCreateRequest, Optional<Company> company) {
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
        try{
            List<City> cities = readCitiesFromJsonFile();
            City city = getCityByName(driverCreateRequest.address().city(), cities);
            Address fullAddress = buildAddress(city, driverCreateRequest.address());
            newDriver.setAddress(fullAddress);

        }catch(IOException e){
            e.printStackTrace();
        }

        newDriver.setAvailable(true);
        return newDriver;
    }

    private Address buildAddress(City city, AddressDTO addressDTO) {
        return Address.builder()
                .city(city.getCity())
                .country(addressDTO.country())
                .street(addressDTO.street())
                .postalCode(addressDTO.postalCode())
                .streetNumber(addressDTO.streetNumber())
                .lat(city.getLat())
                .lng(city.getLng())
                .admin_name(city.getAdmin_name())
                .capital(city.getCapital())
                .iso2(city.getIso2())
                .population(city.getPopulation())
                .population_proper(city.getPopulation_proper())
                .build();
    }
    private  List<City> readCitiesFromJsonFile() throws IOException {
        File jsonFile = new File("C:\\Users\\denis\\OneDrive\\Desktop\\LUCRARE LICENTA\\path-pilot-server\\src\\main\\java\\com\\mycode\\pathpilotserver\\resource\\ro.json");
        return objectMapper.readValue(jsonFile, new TypeReference<List<City>>() {});
    }

    private City getCityByName(String cityName, List<City> cities) {
        return cities.stream()
                .filter(city -> city.getCity().equals(cityName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + cityName));
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
    public void removeByLicenseNumber(String email, String licenseNumber) {
        Optional<Driver> driver = driverRepo.findByLicenseNumber(licenseNumber);
        Optional<User> user = userRepo.findByEmail(email);
        if(driver.isEmpty()) {
            throw new DriverNotFoundException("Driver with license number: " + licenseNumber + " not found");
        }

        if(user.isEmpty()){
            throw new UserNotFoundException("User with email: " + email + " not found");
        }

        if(user.get().getRole()==UserRole.CUSTOMER || user.get().equals(driver.get())){
            driver.ifPresentOrElse(driverRepo::delete, () -> {
                throw new DriverNotFoundException("Driver not found for license number: " + licenseNumber);
            });
        }
        else{
            throw new WrongPasswordException("Invalid password for user: " + user.get().getEmail());
        }
    }


}
