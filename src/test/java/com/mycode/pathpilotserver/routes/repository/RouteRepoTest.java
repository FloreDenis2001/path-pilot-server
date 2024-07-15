package com.mycode.pathpilotserver.routes.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.system.enums.RouteStatus;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class RouteRepoTest {

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    private Company company;
    private Driver driver;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .name("Test Company")
                .registrationNumber("123456789")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.company.com")
                .build();
        companyRepo.save(company);

        driver = Driver.builder()
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
        driverRepo.save(driver);

        vehicle = Vehicle.builder()
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
        vehicleRepo.save(vehicle);

        Route route1 = Route.builder()
                .vehicle(vehicle)
                .driver(driver)
                .company(company)
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(2))
                .totalDistance(100.0)
                .startPoint("Point A")
                .endPoint("Point B")
                .status(RouteStatus.PLANNED)
                .build();

        Route route2 = Route.builder()
                .vehicle(vehicle)
                .driver(driver)
                .company(company)
                .departureDate(LocalDateTime.now().plusDays(3))
                .arrivalTime(LocalDateTime.now().plusDays(4))
                .totalDistance(150.0)
                .startPoint("Point C")
                .endPoint("Point D")
                .status(RouteStatus.COMPLETED)
                .build();

        routeRepo.save(route1);
        routeRepo.save(route2);
    }

    @Test
    void testFindAllByCompanyRegistrationNumber() {
        Optional<List<Route>> routes = routeRepo.findAllByCompanyRegistrationNumber("123456789");
        assertTrue(routes.isPresent());
        assertEquals(2, routes.get().size());
    }

    @Test
    void testSaveRoute() {
        Route route = Route.builder()
                .vehicle(vehicle)
                .driver(driver)
                .company(company)
                .departureDate(LocalDateTime.now().plusDays(5))
                .arrivalTime(LocalDateTime.now().plusDays(6))
                .totalDistance(200.0)
                .startPoint("Point E")
                .endPoint("Point F")
                .status(RouteStatus.PLANNED)
                .build();
        Route savedRoute = routeRepo.save(route);
        assertNotNull(savedRoute.getId());
        assertEquals("Point E", savedRoute.getStartPoint());
    }

    @Test
    void testDeleteRoute() {
        Route route = Route.builder()
                .vehicle(vehicle)
                .driver(driver)
                .company(company)
                .departureDate(LocalDateTime.now().plusDays(5))
                .arrivalTime(LocalDateTime.now().plusDays(6))
                .totalDistance(200.0)
                .startPoint("Point G")
                .endPoint("Point H")
                .status(RouteStatus.PLANNED)
                .build();
        Route savedRoute = routeRepo.save(route);
        routeRepo.deleteById(savedRoute.getId());
        assertFalse(routeRepo.findById(savedRoute.getId()).isPresent());
    }

    @Test
    void testFindById() {
        Route route = routeRepo.findAll().get(0);
        Optional<Route> foundRoute = routeRepo.findById(route.getId());
        assertTrue(foundRoute.isPresent());
        assertEquals(route.getStartPoint(), foundRoute.get().getStartPoint());
    }

    @Test
    void testFindAllRoutes() {
        List<Route> routes = routeRepo.findAll();
        assertEquals(2, routes.size());
    }
}