package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.address.dto.AddressDTO;

import static org.mockito.Mockito.*;

import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.city.utils.Utils;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverCommandServiceImplTest {

    @Mock
    private DriverRepo driverRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CompanyRepo companyRepo;

    @InjectMocks
    private DriverCommandServiceImpl driverCommandService;

    private DriverCreateRequest driverCreateRequest;
    private DriverUpdateRequest driverUpdateRequest;
    private Company company;
    private Driver driver;
    private User user;

    private City city;

    @BeforeEach
    void setUp() {

        company = Company.builder().name("Test Company").registrationNumber("TEST123").industry("Transport").income(1000000.0).phone("0123456789").email("contact@testcompany.com").website("http://testcompany.com").address(null).build();
        driver = Driver.builder().username("driver_user").password("password").email("driver@example.com").role(UserRole.DRIVER).firstName("Driver").lastName("User").phone("0987654321").address(null).company(company).licenseNumber("DL123456").salary(2000.0).isAvailable(true).rating(4.5).experience(5).build();
        user = Customer.builder().email("admin@example.com").role(UserRole.DRIVER).build();

        AddressDTO addressDTO = new AddressDTO("Bucuresti", "17", "Dambovicioarei", "Romania", "447065");
        city = City.builder().city("Bucuresti")
                .lat(44.4325)
                .lng(26.1039)
                .country("Romania")
                .iso2("RO")
                .admin_name("Bucuresti")
                .capital("primary")
                .population("2412530")
                .population_proper("1716961")
                .build();
        driverCreateRequest = new DriverCreateRequest(
                "driver_user",
                "driver@example.com",
                "Driver",
                "User",
                "password",
                "0987654321",
                "DL123456",
                "TEST123",
                addressDTO
        );

        driverUpdateRequest = new DriverUpdateRequest(
                "driver_user",
                "driver@example.com",
                "Driver",
                "User",
                "0987654321",
                2200.0,
                4.0,
                6,
                "DL123456",
                true,
                "TEST123"
        );
    }

    @Test
    void testCreate_DriverCreatedSuccessfully() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumber("DL123456")).thenReturn(Optional.empty());
        when(driverRepo.saveAndFlush(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.getCityByName("Bucuresti")).thenReturn(city);
            driverCommandService.create(driverCreateRequest);
            verify(driverRepo).saveAndFlush(any(Driver.class));
        }
    }
    @Test
    void testCreate_DriverAlreadyExists() {
        when(driverRepo.findByLicenseNumber("DL123456")).thenReturn(Optional.of(driver));
        assertThrows(DriverAlreadyExistException.class, () -> driverCommandService.create(driverCreateRequest));
    }

    @Test
    void testCreate_CompanyNotFound() {
        when(driverRepo.findByLicenseNumber("DL123456")).thenReturn(Optional.empty());
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.empty());
        assertThrows(CompanyNotFoundException.class, () -> driverCommandService.create(driverCreateRequest));
    }

    @Test
    void testUpdate_DriverUpdatedSuccessfully() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumberAndCompany("DL123456", company)).thenReturn(Optional.of(driver));
        when(driverRepo.saveAndFlush(driver)).thenReturn(driver);

        driverCommandService.update(driverUpdateRequest);
    }

    @Test
    void testUpdate_DriverNotFound() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumberAndCompany("DL123456", company)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverCommandService.update(driverUpdateRequest));
    }

    @Test
    void testUpdate_CompanyNotFound() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.empty());
        assertThrows(CompanyNotFoundException.class, () -> driverCommandService.update(driverUpdateRequest));
    }

    @Test
    void testRemoveByLicenseNumber_DriverRemovedSuccessfully() {
        driver.setRoutes(new HashSet<>());

        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumberAndCompany("DL123456", company)).thenReturn(Optional.of(driver));
        when(userRepo.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        driverCommandService.removeByLicenseNumber("admin@example.com", "DL123456", "TEST123");

        verify(driverRepo).delete(driver);
    }

    @Test
    void testRemoveByLicenseNumber_DriverNotFound() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumberAndCompany("DL123456", company)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverCommandService.removeByLicenseNumber("admin@example.com", "DL123456", "TEST123"));
    }

    @Test
    void testRemoveByLicenseNumber_CompanyNotFound() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> driverCommandService.removeByLicenseNumber("admin@example.com", "DL123456", "TEST123"));
    }

    @Test
    void testRemoveByLicenseNumber_UserNotFound() {
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumberAndCompany("DL123456", company)).thenReturn(Optional.of(driver));
        when(userRepo.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> driverCommandService.removeByLicenseNumber("admin@example.com", "DL123456", "TEST123"));
    }

    @Test
    void testRemoveByLicenseNumber_UserNotAuthorized() {
        user.setRole(UserRole.ADMIN);
        when(companyRepo.findByRegistrationNumber("TEST123")).thenReturn(Optional.of(company));
        when(driverRepo.findByLicenseNumberAndCompany("DL123456", company)).thenReturn(Optional.of(driver));
        when(userRepo.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        assertThrows(UserNotFoundException.class, () -> driverCommandService.removeByLicenseNumber("admin@example.com", "DL123456", "TEST123"));
    }
}
