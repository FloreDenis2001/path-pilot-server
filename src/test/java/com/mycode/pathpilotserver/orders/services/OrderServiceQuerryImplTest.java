package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.orders.exceptions.OrderNotFoundException;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.system.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceQuerryImplTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderServiceQuerryImpl orderServiceQuerry;

    private Customer customer;
    private Shipment shipment;
    private Order order;

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

        shipment = Shipment.builder()
                .originName("Origin Name")
                .destinationName("Destination Name")
                .originPhone("000-111-222")
                .destinationPhone("111-222-333")
                .originAddress(null)
                .destinationAddress(null)
                .status(StatusType.PICKED)
                .estimatedDeliveryDate(LocalDateTime.now().plusDays(5))
                .totalDistance(100.0)
                .build();

        order = Order.builder()
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
    }

    @Test
    void testFindByCustomerAndAndShipment_OrderFound() {
        when(orderRepo.findByCustomerAndAndShipment(customer.getId(), shipment.getId())).thenReturn(Optional.of(order));
        Optional<Order> foundOrder = orderServiceQuerry.findByCustomerAndAndShipment(customer.getId(), shipment.getId());
        assertEquals(order, foundOrder.get());
    }

    @Test
    void testFindByCustomerAndAndShipment_OrderNotFound() {
        when(orderRepo.findByCustomerAndAndShipment(customer.getId(), shipment.getId())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderServiceQuerry.findByCustomerAndAndShipment(customer.getId(), shipment.getId()));
    }

    @Test
    void testFindAllByCustomerEmail_OrdersFound() {
        when(orderRepo.findAllByCustomerEmail("customer@example.com")).thenReturn(Optional.of(Arrays.asList(order)));
        Optional<List<Order>> orders = orderServiceQuerry.findAllByCustomerEmail("customer@example.com");
        assertEquals(Arrays.asList(order), orders.get());
    }

    @Test
    void testFindAllByCustomerEmail_OrdersNotFound() {
        when(orderRepo.findAllByCustomerEmail("customer@example.com")).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderServiceQuerry.findAllByCustomerEmail("customer@example.com"));
    }
}
