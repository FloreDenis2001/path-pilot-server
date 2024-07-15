package com.mycode.pathpilotserver.user.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
        companyRepo.deleteAll();
    }

    @BeforeEach
    void setUp() {


        Company company = Company.builder()
                .name("Test Company")
                .registrationNumber("TEST123")
                .industry("Transport")
                .income(1000000.0)
                .phone("0123456789")
                .email("contact@testcompany.com")
                .website("http://testcompany.com")
                .address(null)
                .build();
        companyRepo.save(company);

        Customer customer = Customer.builder()
                .username("customer_user")
                .password("password")
                .email("customer@example.com")
                .role(UserRole.CUSTOMER)
                .firstName("Customer")
                .lastName("User")
                .phone("1234567890")
                .address(null)
                .company(company)
                .subscriptionType(SubscriptionType.BASIC)
                .build();
        userRepo.save(customer);

        Driver driver = Driver.builder()
                .username("driver_user")
                .password("password")
                .email("driver@example.com")
                .role(UserRole.DRIVER)
                .firstName("Driver")
                .lastName("User")
                .phone("0987654321")
                .address(null)
                .company(company)
                .licenseNumber("DL123456")
                .salary(2000.0)
                .isAvailable(true)
                .rating(4.5)
                .experience(5)
                .build();
        userRepo.save(driver);
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        Optional<User> foundUser = userRepo.findByEmail("customer@example.com");
        assertTrue(foundUser.isPresent(), "User should be found by email");
        assertEquals("customer_user", foundUser.get().getUsername(), "Username should match");
        assertTrue(foundUser.get() instanceof Customer, "User should be a Customer");

        Optional<User> foundDriverUser = userRepo.findByEmail("driver@example.com");
        assertTrue(foundDriverUser.isPresent(), "Driver User should be found by email");
        assertEquals("driver_user", foundDriverUser.get().getUsername(), "Username should match");
        assertTrue(foundDriverUser.get() instanceof Driver, "User should be a Driver");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<User> foundUser = userRepo.findByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent(), "No user should be found for non-existent email");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailIsNull() {
        Optional<User> foundUser = userRepo.findByEmail(null);
        assertFalse(foundUser.isPresent(), "No user should be found for null email");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailIsEmpty() {
        Optional<User> foundUser = userRepo.findByEmail("");
        assertFalse(foundUser.isPresent(), "No user should be found for empty email");
    }

    @Test
    void findByEmail_shouldReturnCustomer_whenEmailIsCustomerEmail() {
        Optional<User> foundUser = userRepo.findByEmail("customer@example.com");
        assertTrue(foundUser.isPresent(), "User should be found by email");
        assertTrue(foundUser.get() instanceof Customer, "User should be a Customer");
        Customer customer = (Customer) foundUser.get();
        assertEquals("customer_user", customer.getUsername(), "Username should match");
        assertEquals(SubscriptionType.BASIC, customer.getSubscriptionType(), "Subscription type should match");
    }

    @Test
    void findByEmail_shouldReturnDriver_whenEmailIsDriverEmail() {
        Optional<User> foundUser = userRepo.findByEmail("driver@example.com");
        assertTrue(foundUser.isPresent(), "User should be found by email");
        assertTrue(foundUser.get() instanceof Driver, "User should be a Driver");
        Driver driver = (Driver) foundUser.get();
        assertEquals("driver_user", driver.getUsername(), "Username should match");
        assertEquals("DL123456", driver.getLicenseNumber(), "License number should match");
    }

}
