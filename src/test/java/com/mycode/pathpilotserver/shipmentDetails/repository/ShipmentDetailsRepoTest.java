package com.mycode.pathpilotserver.shipmentDetails.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
@Transactional
class ShipmentDetailsRepoTest {
    @Autowired
    private ShipmentRepo shipmentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private ShipmentDetailsRepo shipmentDetailsRepo;

    @BeforeEach
    void setUp() {
        shipmentDetailsRepo.deleteAllInBatch();
        shipmentRepo.deleteAllInBatch();
        driverRepo.deleteAllInBatch();
        vehicleRepo.deleteAllInBatch();
        userRepo.deleteAllInBatch();
    }
    @Test
    void findByShipmentId() {
        Optional<ShipmentDetail> found = shipmentDetailsRepo.findByShipmentId(createTestShipmentDetail().getShipment().getId());
        assertTrue(found.isPresent());
        assertEquals(createTestShipmentDetail().getArrivalTime(), found.get().getArrivalTime());
    }

    @Test
    void findByArrivalTime() {
        Optional<ShipmentDetail> found = shipmentDetailsRepo.findByArrivalTime(createTestShipmentDetail().getArrivalTime());
        assertFalse(found.isEmpty());
//        assertTrue(found.stream().anyMatch(detail -> detail.getId().equals(createTestShipmentDetail().getId())));
        assertEquals(createTestShipmentDetail().getArrivalTime(), found.get().getArrivalTime());
    }




    private ShipmentDetail createTestShipmentDetail() {
        User user = userRepo.save(new User("Username", "Password", "Email", "Role"));
        Driver driver = driverRepo.save(new Driver("Driver Name", "123456789", "License123", user));
        Vehicle vehicle = vehicleRepo.save(new Vehicle("Vehicle Type", "REG123", 5000));
        Shipment shipment = shipmentRepo.save(new Shipment("Origin", "Destination", "Pending", LocalDateTime.of(2009,1,5,14,12,1).plusDays(1)));

        return shipmentDetailsRepo.save(new ShipmentDetail(shipment, driver, vehicle, LocalDateTime.of(2009,1,5,14,12,1), LocalDateTime.of(2009,1,5,14,12,1).plusDays(10)));
    }

}