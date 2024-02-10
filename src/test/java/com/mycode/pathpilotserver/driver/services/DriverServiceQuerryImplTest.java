package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.driver.exceptions.DriverNotFoundException;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DriverServiceQuerryImplTest {

    @Mock
    private DriverRepo driverRepo;

    private DriverServiceQuerry driverServiceQuerry;

    @BeforeEach
    void setUp() {
        driverServiceQuerry = new DriverServiceQuerryImpl(driverRepo);
    }

    private Driver createTestDriver() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("Driver Name");
        driver.setLicenseNumber("License123");
        driver.setPhone("123456789");
        return driver;
    }

    @Test
    void findByName() {
        Optional<Driver> driver = Optional.of(createTestDriver());
        doReturn(driver).when(driverRepo).findByName("Driver Name");
        assertEquals(driver.get(), driverServiceQuerry.findByName("Driver Name").get());
    }

    @Test
    void findByNameException(){
        doReturn(Optional.empty()).when(driverRepo).findByName("Driver Name");
        assertThrows(DriverNotFoundException.class, () -> driverServiceQuerry.findByName("Driver Name").get());
    }

    @Test
    void findByUserEmail() {
        Optional<Driver> driver = Optional.of(createTestDriver());
        doReturn(driver).when(driverRepo).findByUserEmail("Email Test");
        assertEquals(driver.get(), driverServiceQuerry.findByUserEmail("Email Test").get());
    }

    @Test
    void findByUserEmailException(){
        doReturn(Optional.empty()).when(driverRepo).findByUserEmail("Email Test");
        assertThrows(DriverNotFoundException.class, () -> driverServiceQuerry.findByUserEmail("Email Test").get());
    }

}