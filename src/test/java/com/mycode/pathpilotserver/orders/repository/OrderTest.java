package com.mycode.pathpilotserver.orders.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;

import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
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
class OrderTest {

    @Autowired
    private OrderRepo orderRepo;

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

        Order order = Order.builder()
                .awb("ABC123")
                .weight(1.0)
                .height(1.0)
                .width(1.0)
                .length(1.0)
                .deliveryDescription("Test Description")
                .orderDate(LocalDateTime.now())
                .totalAmount(100.0)
                .deliverySequence(1)
                .customer(customer)
                .shipment(shipment)
                .route(null)
                .build();

        orderRepo.save(order);
    }
    @Test
    void testFindByCustomerAndShipment() {
        Order foundOrder = orderRepo.findByCustomerAndAndShipment(customer.getId(), shipment.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getAwb()).isEqualTo("ABC123");
        assertThat(foundOrder.getCustomer().getId()).isEqualTo(customer.getId());
        assertThat(foundOrder.getShipment().getId()).isEqualTo(shipment.getId());
    }

    @Test
    void testFindAllByCustomerEmail() {
        List<Order> orders = orderRepo.findAllByCustomerEmail("customer@example.com")
                .orElseThrow(() -> new RuntimeException("No orders found for the email"));

        assertThat(orders).isNotEmpty();
        assertThat(orders.size()).isGreaterThan(0);
        assertThat(orders.get(0).getCustomer().getEmail()).isEqualTo("customer@example.com");
    }

}