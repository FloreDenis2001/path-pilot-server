package com.mycode.pathpilotserver.driver.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.shipmentDetails.repository.ShipmentDetailsRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
class DriverRepoTest {

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShipmentDetailsRepo shipmentDetailsRepo;

    @BeforeEach
    void setUp() {
        driverRepo.deleteAll();
        userRepo.deleteAll();
        shipmentDetailsRepo.deleteAll();
    }

    @Test
    void findByName() {
        User user = new User();
        user.setEmail(" Email Test");
        user.setPassword("Password Test");
        user.setRole("Role Test");
        user.setUsername("Username Test");
        userRepo.saveAndFlush(user);

        Driver driver = new Driver();
        driver.setPhone("Phone Number Test");
        driver.setUser(user);
        driver.setName("Name Test");
        driver.setLicenseNumber("License Number Test");
        driverRepo.saveAndFlush(driver);

        Driver driver1 = driverRepo.findByName("Name Test");
        assertEquals(driver1.getName(), driver.getName());
    }

    @Test
    void findByUserEmail() {

        User user = new User();
        user.setEmail(" Email Test");
        user.setPassword("Password Test");
        user.setRole("Role Test");
        user.setUsername("Username Test");
        userRepo.saveAndFlush(user);

        Driver driver = new Driver();
        driver.setPhone("Phone Number Test");
        driver.setUser(user);
        driver.setName("Name Test");
        driver.setLicenseNumber("License Number Test");
        driverRepo.saveAndFlush(driver);

        Driver driver1 = driverRepo.findByUserEmail(" Email Test");
        assertEquals(driver1.getUser().getEmail(), driver.getUser().getEmail());
    }
}