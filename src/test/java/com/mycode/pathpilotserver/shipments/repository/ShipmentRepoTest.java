package com.mycode.pathpilotserver.shipments.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
@Transactional
class ShipmentRepoTest {

    @Autowired
    private ShipmentRepo shipmentRepo;


    @BeforeEach
    void setUp() {
        shipmentRepo.deleteAll();
    }
    private Shipment createTestShipment() {
        Address originAddress= new Address("Romania","Satu Mare","Grivitei","17A","5214");

        return shipmentRepo.save(new Shipment(originAddress,originAddress, "Pending", LocalDateTime.of(2009,1,5,14,12,1).plusDays(1)));
    }
    @Test
    void findByOrigin() {
        Optional<Shipment> shipment1 = shipmentRepo.findShipmentByOriginAddress(createTestShipment().getOriginAddress());
        assertTrue(shipment1.isPresent());
        assertEquals(createTestShipment().getOriginAddress().getCity(), shipment1.get().getOriginAddress().getCity());

    }
}