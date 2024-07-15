package com.mycode.pathpilotserver.driver.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.system.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class DriverTest {
    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private CompanyRepo companyRepo;

    private Company company;

    @BeforeEach
    public void setUp() {
        company = Company.builder()
                .name("Test Company")
                .registrationNumber("12345")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.company.com")
                .build();
        companyRepo.save(company);

        Driver driver1 = Driver.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john_doe")
                .password("password")
                .email("john.doe@example.com")
                .phone("1234567890")
                .role(UserRole.DRIVER)
                .company(company)
                .licenseNumber("ABC123")
                .salary(50000)
                .isAvailable(true)
                .rating(4.5)
                .experience(5)
                .build();

        Driver driver2 = Driver.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("jane_smith")
                .password("password")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .role(UserRole.DRIVER)
                .company(company)
                .licenseNumber("XYZ789")
                .salary(60000)
                .isAvailable(false)
                .rating(4.8)
                .experience(7)
                .build();

        driverRepo.save(driver1);
        driverRepo.save(driver2);
    }

    @Test
    public void testFindByLicenseNumber() {
        Optional<Driver> foundDriver = driverRepo.findByLicenseNumber("ABC123");
        assertThat(foundDriver).isPresent();
        assertThat(foundDriver.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindAllByCompanyRegistrationNumber() {
        Optional<List<Driver>> foundDrivers = driverRepo.findAllByCompanyRegistrationNumber("12345");
        assertThat(foundDrivers).isPresent();
        assertThat(foundDrivers.get()).hasSize(2);
    }

    @Test
    public void testFindAllByCompanyRegistrationNumberAndIsAvailableTrue() {
        Optional<List<Driver>> foundDrivers = driverRepo.findAllByCompanyRegistrationNumberAndIsAvailableTrue("12345");
        assertThat(foundDrivers).isPresent();
        assertThat(foundDrivers.get()).hasSize(1);
        assertThat(foundDrivers.get().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void testBestDriversByHighestRanking() {
        Optional<List<Driver>> foundDrivers = driverRepo.bestDriversByHighestRanking("12345");
        assertThat(foundDrivers).isPresent();
        assertThat(foundDrivers.get()).hasSize(2);
        assertThat(foundDrivers.get().get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    public void testFindByLicenseNumberAndCompany() {
        Optional<Driver> foundDriver = driverRepo.findByLicenseNumberAndCompany("ABC123", company);
        assertThat(foundDriver).isPresent();
        assertThat(foundDriver.get().getFirstName()).isEqualTo("John");
    }
}