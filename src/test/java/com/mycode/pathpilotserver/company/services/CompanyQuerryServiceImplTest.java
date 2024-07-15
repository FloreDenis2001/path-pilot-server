package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.system.enums.PackageStatus;
import com.mycode.pathpilotserver.system.security.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CompanyQuerryServiceImplTest {

    @Mock
    private CompanyRepo companyRepo;

    @Mock
    private PackageRepo packageRepo;

    @Mock
    private DriverRepo driverRepo;

    @InjectMocks
    private CompanyQuerryServiceImpl companyQuerryService;


    @Test
    void testFindByName_Success() {
        Company company = new Company();
        company.setName("Test Company");

        when(companyRepo.findByName("Test Company")).thenReturn(Optional.of(company));

        Optional<Company> result = companyQuerryService.findByName("Test Company");

        assertTrue(result.isPresent(), "Company should be found.");
        assertEquals("Test Company", result.get().getName());
    }

    @Test
    void testFindByName_Failure() {
        when(companyRepo.findByName("Nonexistent Company")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.findByName("Nonexistent Company")
        );

        assertEquals("Company with name: Nonexistent Company not found", thrown.getMessage());
    }

    @Test
    void testFindByEmail_Success() {
        Company company = new Company();
        company.setEmail("test@company.com");

        when(companyRepo.findByEmail("test@company.com")).thenReturn(Optional.of(company));

        Optional<Company> result = companyQuerryService.findByEmail("test@company.com");

        assertTrue(result.isPresent(), "Company should be found.");
        assertEquals("test@company.com", result.get().getEmail());
    }

    @Test
    void testFindByEmail_Failure() {
        when(companyRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.findByEmail("nonexistent@example.com")
        );

        assertEquals("Company with email: nonexistent@example.com not found", thrown.getMessage());
    }

    @Test
    void testFindByPhone_Success() {
        Company company = new Company();
        company.setPhone("123-456-7890");

        when(companyRepo.findByPhone("123-456-7890")).thenReturn(Optional.of(company));

        Optional<Company> result = companyQuerryService.findByPhone("123-456-7890");

        assertTrue(result.isPresent(), "Company should be found.");
        assertEquals("123-456-7890", result.get().getPhone());
    }

    @Test
    void testFindByPhone_Failure() {
        when(companyRepo.findByPhone("000-000-0000")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.findByPhone("000-000-0000")
        );

        assertEquals("Company with phone: 000-000-0000 not found", thrown.getMessage());
    }

    @Test
    void testFindByRegistrationNumber_Success() {
        City city = City.builder()
                .country("Romania")
                .city("Bucuresti")
                .lat(44.4268)
                .lng(26.1025)
                .iso2("RO")
                .admin_name("Bucharest")
                .capital("Bucharest")
                .population("1877155")
                .population_proper("1877155")
                .build();

        Address address = Address.builder()
                .street("Damovicioarei")
                .streetNumber("17")
                .postalCode("447065")
                .cityDetails(city)
                .build();

        Company company = Company.builder()
                .name("Test Company")
                .registrationNumber("REG123")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.company.com")
                .address(address)
                .build();


        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.of(company));

        Optional<CompanyDTO> result = companyQuerryService.findByRegistrationNumber("REG123");

        assertTrue(result.isPresent(), "CompanyDTO should be found.");
        assertEquals("REG123", result.get().registrationNumber());
    }

    @Test
    void testFindByRegistrationNumber_Failure() {
        when(companyRepo.findByRegistrationNumber("NONEXISTENT")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.findByRegistrationNumber("NONEXISTENT")
        );

        assertEquals("Company with registration number: NONEXISTENT not found", thrown.getMessage());
    }

    @Test
    void testFindCompaniesByIndustry_Success() {
        Company company = new Company();
        company.setIndustry("Tech");

        when(companyRepo.findCompaniesByIndustry("Tech")).thenReturn(Optional.of(List.of(company)));

        Optional<List<Company>> result = companyQuerryService.findCompaniesByIndustry("Tech");

        assertTrue(result.isPresent(), "Companies should be found.");
        assertEquals("Tech", result.get().get(0).getIndustry());
    }

    @Test
    void testFindCompaniesByIndustry_Failure() {
        when(companyRepo.findCompaniesByIndustry("NonexistentIndustry")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.findCompaniesByIndustry("NonexistentIndustry")
        );

        assertEquals("Companies in industry: NonexistentIndustry not found", thrown.getMessage());
    }

    @Test
    void testGetTotalSumLastMonthPackages_Success() {
        Package pkg1 = new Package();
        pkg1.setOrderDate(LocalDateTime.now().minusWeeks(2));
        pkg1.setTotalAmount(100.0);

        Package pkg2 = new Package();
        pkg2.setOrderDate(LocalDateTime.now().minusWeeks(6));
        pkg2.setTotalAmount(150.0);

        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.of(List.of(pkg1, pkg2)));

        double result = companyQuerryService.getTotalSumLastMonthPackages("REG123");

        assertEquals(100.0, result, "Total sum of packages should be correct.");
    }

    @Test
    void testGetTotalSumLastMonthPackages_Failure() {
        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.empty());

        PackageNotFoundException thrown = assertThrows(PackageNotFoundException.class, () ->
                companyQuerryService.getTotalSumLastMonthPackages("REG123")
        );

        assertEquals("Packages for company with registration number: REG123 not found", thrown.getMessage());
    }

    @Test
    void testGetTotalNumberOfPackagesLastMonth_Success() {
        Package pkg1 = new Package();
        pkg1.setOrderDate(LocalDateTime.now().minusWeeks(2));

        Package pkg2 = new Package();
        pkg2.setOrderDate(LocalDateTime.now().minusWeeks(8));

        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.of(List.of(pkg1, pkg2)));

        int result = companyQuerryService.getTotalNumberOfPackagesLastMonth("REG123");

        assertEquals(1, result, "Total number of packages should be correct.");
    }

    @Test
    void testGetTotalNumberOfPackagesLastMonth_Failure() {
        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.empty());

        PackageNotFoundException thrown = assertThrows(PackageNotFoundException.class, () ->
                companyQuerryService.getTotalNumberOfPackagesLastMonth("REG123")
        );

        assertEquals("Packages for company with registration number: REG123 not found", thrown.getMessage());
    }

    @Test
    void testGetTotalSumLastMonthOfSalary_Success() {
        Driver driver1 = new Driver();
        driver1.setSalary(2000.0);

        Driver driver2 = new Driver();
        driver2.setSalary(2500.0);

        when(driverRepo.findAllByCompanyRegistrationNumber("REG123")).thenReturn(Optional.of(List.of(driver1, driver2)));

        double result = companyQuerryService.getTotalSumLastMonthOfSalary("REG123");

        assertEquals(4500.0, result, "Total sum of drivers' salaries should be correct.");
    }

    @Test
    void testGetTotalSumLastMonthOfSalary_Failure() {
        when(driverRepo.findAllByCompanyRegistrationNumber("REG123")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.getTotalSumLastMonthOfSalary("REG123")
        );

        assertEquals("Drivers for company with registration number: REG123 not found", thrown.getMessage());
    }

    @Test
    void testGetTotalSumLastMonthProfit_Success() {
        Package pkg1 = new Package();
        pkg1.setOrderDate(LocalDateTime.now().minusWeeks(2));
        pkg1.setTotalAmount(1000.0);

        Package pkg2 = new Package();
        pkg2.setOrderDate(LocalDateTime.now().minusWeeks(3));
        pkg2.setTotalAmount(1500.0);

        Driver driver1 = new Driver();
        driver1.setSalary(2000.0);

        Driver driver2 = new Driver();
        driver2.setSalary(2500.0);

        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.of(List.of(pkg1, pkg2)));
        when(driverRepo.findAllByCompanyRegistrationNumber("REG123")).thenReturn(Optional.of(List.of(driver1, driver2)));

        double result = companyQuerryService.getTotalSumLastMonthProfit("REG123");

        assertEquals(-2000.0, result, "Total profit calculation should be correct.");
    }

    @Test
    void testGetBestFiveDriversByRanking_Success() {
        Driver driver1 = new Driver();
        driver1.setRating(5.0);

        Driver driver2 = new Driver();
        driver2.setRating(4.5);

        Driver driver3 = new Driver();
        driver3.setRating(4.0);

        Driver driver4 = new Driver();
        driver4.setRating(3.5);

        Driver driver5 = new Driver();
        driver5.setRating(3.0);

        Driver driver6 = new Driver();
        driver6.setRating(2.5);

        when(driverRepo.bestDriversByHighestRanking("REG123")).thenReturn(Optional.of(List.of(driver1, driver2, driver3, driver4, driver5, driver6)));

        Optional<List<DriverDTO>> result = companyQuerryService.getBestFiveDriversByRanking("REG123");

        assertTrue(result.isPresent(), "Best five drivers should be found.");
        assertEquals(5, result.get().size(), "There should be 5 best drivers.");
    }

    @Test
    void testGetBestFiveDriversByRanking_Failure() {
        when(driverRepo.bestDriversByHighestRanking("REG123")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyQuerryService.getBestFiveDriversByRanking("REG123")
        );

        assertEquals("Drivers for company with registration number: REG123 not found", thrown.getMessage());
    }

    @Test
    void testLastFivePackagesAdded_Success() {
        Company company= Company.builder()
                .name("Test Company")
                .registrationNumber("123456789")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.company.com")
                .build();
        City city1 = City.builder()
                .country("Romania")
                .city("Bucuresti")
                .lat(44.4268)
                .lng(26.1025)
                .iso2("RO")
                .admin_name("Bucharest")
                .capital("Bucharest")
                .population("1877155")
                .population_proper("1877155")
                .build();

        Address originAddress = Address.builder()
                .street("Damovicioarei")
                .streetNumber("17")
                .postalCode("447065")
                .cityDetails(city1)
                .build();

        City city2 = City.builder()
                .country("Romania")
                .city("Brasov")
                .lat(44.4268)
                .lng(26.1025)
                .iso2("RO")
                .admin_name("Bucharest")
                .capital("Bucharest")
                .population("1877155")
                .population_proper("1877155")
                .build();

        Address destinationAddress = Address.builder()
                .street("Strada Lunga")
                .streetNumber("23")
                .postalCode("500123")
                .cityDetails(city2)
                .build();

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

        Shipment shipment1 = Shipment.builder()
                .originName("Origin Name 1")
                .destinationName("Destination Name 1")
                .originPhone("000-111-222")
                .destinationPhone("111-222-333")
                .originAddress(originAddress)
                .destinationAddress(destinationAddress)
                .status(StatusType.PICKED)
                .estimatedDeliveryDate(LocalDateTime.now().plusDays(5))
                .totalDistance(100.0)
                .build();

        Shipment shipment2 = Shipment.builder()
                .originName("Origin Name 2")
                .destinationName("Destination Name 2")
                .originPhone("000-111-223")
                .destinationPhone("111-222-334")
                .originAddress(originAddress)
                .destinationAddress(destinationAddress)
                .status(StatusType.CANCELLED)
                .estimatedDeliveryDate(LocalDateTime.now().plusDays(4))
                .totalDistance(150.0)
                .build();

        Package pkg1 = Package.builder()
                .awb("XYZ789")
                .weight(2.0)
                .height(2.0)
                .width(2.0)
                .length(2.0)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription("Test Package 1 Description")
                .orderDate(LocalDateTime.now().minusDays(1))
                .totalAmount(200.0)
                .customer(customer)
                .shipment(shipment1)
                .build();

        Package pkg2 = Package.builder()
                .awb("XYZ788")
                .weight(2.0)
                .height(2.0)
                .width(2.0)
                .length(2.0)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription("Test Package 2 Description")
                .orderDate(LocalDateTime.now().minusDays(2))
                .totalAmount(250.0)
                .customer(customer)
                .shipment(shipment1)
                .build();

        Package pkg3 = Package.builder()
                .awb("XYZ787")
                .weight(2.0)
                .height(2.0)
                .width(2.0)
                .length(2.0)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription("Test Package 3 Description")
                .orderDate(LocalDateTime.now().minusDays(3))
                .totalAmount(300.0)
                .customer(customer)
                .shipment(shipment1)
                .build();

        Package pkg4 = Package.builder()
                .awb("XYZ786")
                .weight(2.0)
                .height(2.0)
                .width(2.0)
                .length(2.0)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription("Test Package 4 Description")
                .orderDate(LocalDateTime.now().minusDays(4))
                .totalAmount(150.0)
                .customer(customer)
                .shipment(shipment2)
                .build();

        Package pkg5 = Package.builder()
                .awb("XYZ785")
                .weight(2.0)
                .height(2.0)
                .width(2.0)
                .length(2.0)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription("Test Package 5 Description")
                .orderDate(LocalDateTime.now().minusDays(5))
                .totalAmount(180.0)
                .customer(customer)
                .shipment(shipment2)
                .build();

        Package pkg6 = Package.builder()
                .awb("XYZ784")
                .weight(2.0)
                .height(2.0)
                .width(2.0)
                .length(2.0)
                .status(PackageStatus.UNASSIGNED)
                .deliveryDescription("Test Package 6 Description")
                .orderDate(LocalDateTime.now().minusDays(6))
                .totalAmount(220.0)
                .customer(customer)
                .shipment(shipment2)
                .build();

        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.of(List.of(pkg1, pkg2, pkg3, pkg4, pkg5, pkg6)));

        Optional<List<PackageDTO>> result = companyQuerryService.lastFivePackagesAdded("REG123");

        assertTrue(result.isPresent(), "Last five packages should be found.");
        assertEquals(5, result.get().size(), "There should be 5 packages.");
        assertEquals("Test Package 1 Description", result.get().get(0).packageDetails().deliveryDescription(), "First package's description should be correct.");
        assertEquals("Test Package 2 Description", result.get().get(1).packageDetails().deliveryDescription(), "Second package's description should be correct.");
        assertEquals("Test Package 3 Description", result.get().get(2).packageDetails().deliveryDescription(), "Third package's description should be correct.");
        assertEquals("Test Package 4 Description", result.get().get(3).packageDetails().deliveryDescription(), "Fourth package's description should be correct.");
        assertEquals("Test Package 5 Description", result.get().get(4).packageDetails().deliveryDescription(), "Fifth package's description should be correct.");
    }


    @Test
    void testLastFivePackagesAdded_Failure() {
        when(packageRepo.getAllByRegisterCompany("REG123")).thenReturn(Optional.empty());

        PackageNotFoundException thrown = assertThrows(PackageNotFoundException.class, () ->
                companyQuerryService.lastFivePackagesAdded("REG123")
        );

        assertEquals("Packages for company with registration number: REG123 not found", thrown.getMessage());
    }
}
