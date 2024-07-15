package com.mycode.pathpilotserver.packages.models;


import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.system.enums.PackageStatus;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class PackageTest {

        @Autowired
        private PackageRepo packageRepo;

        @Autowired
        private CompanyRepo companyRepo;

        @Autowired
        private ShipmentRepo shipmentRepo;

        @Autowired
        private UserRepo userRepo;

        private Customer customer;
        private Shipment shipment;

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

            customer = Customer.builder()
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

            Address originAddress = Address.builder()
                    .street("Damovicioarei")
                    .streetNumber("17")
                    .postalCode("447065")
                    .cityDetails(city)
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

            shipment = Shipment.builder()
                    .originName("Origin Name")
                    .destinationName("Destination Name")
                    .originPhone("000-111-222")
                    .destinationPhone("111-222-333")
                    .originAddress(originAddress)
                    .destinationAddress(destinationAddress)
                    .status(StatusType.PICKED)
                    .estimatedDeliveryDate(LocalDateTime.now().plusDays(5))
                    .totalDistance(100.0)
                    .build();
            shipmentRepo.save(shipment);

            Package pack = Package.builder()
                    .awb("XYZ789")
                    .weight(2.0)
                    .height(2.0)
                    .width(2.0)
                    .length(2.0)
                    .status(PackageStatus.UNASSIGNED)
                    .deliveryDescription("Test Package Description")
                    .orderDate(LocalDateTime.now())
                    .totalAmount(200.0)
                    .customer(customer)
                    .shipment(shipment)
                    .build();
            packageRepo.save(pack);
        }

        @Test
        void testFindByCustomerAndAndShipment() {
            Package foundPackage = packageRepo.findByCustomerAndAndShipment(customer.getId(), shipment.getId())
                    .orElseThrow(() -> new RuntimeException("Package not found"));

            assertThat(foundPackage).isNotNull();
            assertThat(foundPackage.getAwb()).isEqualTo("XYZ789");
            assertThat(foundPackage.getCustomer().getId()).isEqualTo(customer.getId());
            assertThat(foundPackage.getShipment().getId()).isEqualTo(shipment.getId());
        }

        @Test
        void testGetAllPackagesByCustomer() {
            List<Package> packages = packageRepo.getAllPackagesByCustomer(customer.getId())
                    .orElseThrow(() -> new RuntimeException("No packages found for the customer"));

            assertThat(packages).isNotEmpty();
            assertThat(packages.size()).isGreaterThan(0);
            assertThat(packages.get(0).getCustomer().getId()).isEqualTo(customer.getId());
        }

        @Test
        void testGetPackageByAwb() {
            Package foundPackage = packageRepo.getPackageByAwb("XYZ789")
                    .orElseThrow(() -> new RuntimeException("Package not found by AWB"));

            assertThat(foundPackage).isNotNull();
            assertThat(foundPackage.getAwb()).isEqualTo("XYZ789");
        }

        @Test
        void testGetAllUnassignedPackages() {
            List<Package> packages = packageRepo.getAllUnassignedPackages("TEST123")
                    .orElseThrow(() -> new RuntimeException("No unassigned packages found for the company"));

            assertThat(packages).isNotEmpty();
            assertThat(packages.size()).isGreaterThan(0);
            assertThat(packages.get(0).getStatus()).isEqualTo(PackageStatus.UNASSIGNED);
        }

        @Test
        void testGetAllUnassignedPackagesByCityAndCompany() {
            List<Package> packages = packageRepo.getAllUnassignedPackagesByCityAndCompany("TEST123", "Bucuresti")
                    .orElseThrow(() -> new RuntimeException("No unassigned packages found for the city and company"));

            assertThat(packages).isNotEmpty();
            assertThat(packages.size()).isGreaterThan(0);
            assertThat(packages.get(0).getStatus()).isEqualTo(PackageStatus.UNASSIGNED);
            assertThat(packages.get(0).getShipment().getOriginAddress().getCityDetails().getCity()).isEqualTo("Bucuresti");
        }

        @Test
        void testGetAllByRegisterCompany() {
            List<Package> packages = packageRepo.getAllByRegisterCompany("TEST123")
                    .orElseThrow(() -> new RuntimeException("No packages found for the company"));

            assertThat(packages).isNotEmpty();
            assertThat(packages.size()).isGreaterThan(0);
            assertThat(packages.get(0).getCustomer().getCompany().getRegistrationNumber()).isEqualTo("TEST123");
        }

    @Test
    void testGetAllUnassignedPackagesWithDifferentRegistrationNumber() {
        List<Package> packages = packageRepo.getAllUnassignedPackages("WRONG123")
                .orElseThrow(() -> new RuntimeException("Packages should be empty for a non-existing registration number"));

        assertThat(packages).isEmpty();
    }

    @Test
    void testGetAllUnassignedPackagesByCityAndCompanyWithDifferentCity() {
        List<Package> packages = packageRepo.getAllUnassignedPackagesByCityAndCompany("TEST123", "Brasov")
                .orElseThrow(() -> new RuntimeException("No unassigned packages found for the city and company"));

        assertThat(packages).isEmpty();
    }

    @Test
    void testGetAllPackagesByCustomerWithInvalidCustomerId() {
        List<Package> packages = packageRepo.getAllPackagesByCustomer(-1L)
                .orElseThrow(() -> new RuntimeException("Packages should be empty for a non-existing customer ID"));

        assertThat(packages).isEmpty();
    }



    @Test
    void testGetAllByRegisterCompanyWithDifferentCompany() {
        List<Package> packages = packageRepo.getAllByRegisterCompany("DIFFERENT_COMPANY")
                .orElseThrow(() -> new RuntimeException("Packages should be empty for a non-existing registration number"));

        assertThat(packages).isEmpty();
    }
    }

