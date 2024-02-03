package com.mycode.pathpilotserver.orders.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
@Transactional
class OrderRepoTest {

    @Autowired
    private OrderRepo orderRepo;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
    }

    private Order createTestOrder() {
        Shipment shipment = Shipment.builder().id(1L).destination("Destination").origin("Origin").estimatedDeliveryDate(LocalDateTime.of(2024,12,12,14,20,0)).build();
        Customer customer = Customer.builder().id(1L).name("Name").address("Address").phone("Phone").build();
        return orderRepo.save(Order.builder().totalAmount(100.0).shipment(shipment).customer(customer).orderDate(LocalDateTime.now()).build());
    }

    @Test
    void findByCustomerName() {
        Order order = createTestOrder();
        Order order1 = orderRepo.findByCustomerName(order.getCustomer().getName()).get();
        assertEquals(order.getCustomer().getName(), order1.getCustomer().getName());
    }

}
