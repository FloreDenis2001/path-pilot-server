package com.mycode.pathpilotserver.vehicles.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class VehicleRepoTest {


    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private CompanyRepo companyRepo;

    private Vehicle testVehicle;

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
                .build();
        companyRepo.save(company);

        testVehicle = Vehicle.builder()
                .make("Toyota")
                .model("Corolla")
                .year(2022)
                .fuelType(FuelType.PETROL)
                .fuelCapacity(50.0)
                .fuelConsumption(5.0)
                .lastService(LocalDate.of(2023, 1, 1))
                .nextService(LocalDate.of(2024, 1, 1))
                .km(15000.0)
                .registrationNumber("ABC123")
                .capacity(5)
                .width(1.8)
                .height(1.5)
                .length(4.5)
                .weight(1300.0)
                .isActive(true)
                .currentLocation(null)
                .company(company)
                .build();

        vehicleRepo.save(testVehicle);
    }

    @Test
    void findByRegistrationNumber_ShouldReturnVehicle() {
        Optional<Vehicle> foundVehicle = vehicleRepo.findByRegistrationNumber("ABC123");
        assertTrue(foundVehicle.isPresent());
        assertEquals(testVehicle.getRegistrationNumber(), foundVehicle.get().getRegistrationNumber());
    }




}