package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.system.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverServiceQuerryImplTest {

    @Mock
    private DriverRepo driverRepo;

    @InjectMocks
    private DriverServiceQuerryImpl driverServiceQuerry;

    private Company company;
    private Driver driver1;
    private Driver driver2;

    @BeforeEach
    void setUp() {

        company = Company.builder().name("Test Company").registrationNumber("TEST123").industry("Transport").income(1000000.0).phone("0123456789").email("contact@testcompany.com").website("http://testcompany.com").address(null).build();

        driver1 = Driver.builder().username("driver1_user").password("password").email("driver1@example.com").role(UserRole.DRIVER).firstName("Driver1").lastName("User1").phone("0987654321").address(null).company(company).licenseNumber("DL123456").salary(2000.0).isAvailable(true).rating(4.5).experience(5).build();

        driver2 = Driver.builder().username("driver2_user").password("password").email("driver2@example.com").role(UserRole.DRIVER).firstName("Driver2").lastName("User2").phone("1234567890").address(null).company(company).licenseNumber("DL654321").salary(2200.0).isAvailable(false).rating(4.8).experience(6).build();
    }

    @Test
    void testGetAllDrivers_DriversFound() {
        List<Driver> drivers = List.of(driver1, driver2);
        when(driverRepo.findAll()).thenReturn(drivers);

        Optional<List<DriverDTO>> result = driverServiceQuerry.getAllDrivers();

        assertEquals(2, result.get().size());
        assertEquals("Driver1", result.get().get(0).firstName());
        assertEquals("Driver2", result.get().get(1).firstName());
    }

    @Test
    void testGetAllDrivers_NoDriversFound() {
        when(driverRepo.findAll()).thenReturn(List.of());
        assertThrows(DriverNotFoundException.class, () -> driverServiceQuerry.getAllDrivers());
    }

    @Test
    void testGetAllDrivers_ExceptionThrownFromRepo() {
        when(driverRepo.findAll()).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> driverServiceQuerry.getAllDrivers());
    }

    @Test
    void testGetAllDriversByCompany_DriversFound() {
        List<Driver> drivers = List.of(driver1, driver2);
        when(driverRepo.findAllByCompanyRegistrationNumber("TEST123")).thenReturn(Optional.of(drivers));

        Optional<List<DriverDTO>> result = driverServiceQuerry.getAllDriversByCompany("TEST123");

        assertEquals(2, result.get().size());
        assertEquals("Driver1", result.get().get(0).firstName());
        assertEquals("Driver2", result.get().get(1).firstName());
    }

    @Test
    void testGetAllDriversByCompany_NoDriversFound() {
        when(driverRepo.findAllByCompanyRegistrationNumber("TEST123")).thenReturn(Optional.empty());
        assertThrows(DriverNotFoundException.class, () -> driverServiceQuerry.getAllDriversByCompany("TEST123"));
    }

    @Test
    void testGetAllDriversByCompany_ExceptionThrownFromRepo() {
        when(driverRepo.findAllByCompanyRegistrationNumber("TEST123")).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> driverServiceQuerry.getAllDriversByCompany("TEST123"));
    }

    @Test
    void testFindByLicenseNumber_DriverFound() {
        when(driverRepo.findByLicenseNumber("DL123456")).thenReturn(Optional.of(driver1));
        Optional<Driver> result = driverServiceQuerry.findByLicenseNumber("DL123456");
        assertEquals("Driver1", result.get().getFirstName());
    }

    @Test
    void testFindByLicenseNumber_DriverNotFound() {
        when(driverRepo.findByLicenseNumber("INVALID_LICENSE")).thenReturn(Optional.empty());
        assertThrows(DriverNotFoundException.class, () -> driverServiceQuerry.findByLicenseNumber("INVALID_LICENSE"));
    }

    @Test
    void testFindByLicenseNumber_ExceptionThrownFromRepo() {
        when(driverRepo.findByLicenseNumber("DL123456")).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> driverServiceQuerry.findByLicenseNumber("DL123456"));
    }

    @Test
    void testFindByLicenseNumber_InvalidLicenseNumber() {
        when(driverRepo.findByLicenseNumber("INVALID_LICENSE")).thenReturn(Optional.empty());
        assertThrows(DriverNotFoundException.class, () -> driverServiceQuerry.findByLicenseNumber("INVALID_LICENSE"));
    }
}
