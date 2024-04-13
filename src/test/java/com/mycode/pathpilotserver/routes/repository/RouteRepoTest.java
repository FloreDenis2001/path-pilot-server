package com.mycode.pathpilotserver.routes.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
@Transactional
class RouteRepoTest {
//    @Autowired
//    private ShipmentRepo shipmentRepo;
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private VehicleRepo vehicleRepo;
//
//    @Autowired
//    private DriverRepo driverRepo;
//
//    @Autowired
//    private ShipmentDetailsRepo shipmentDetailsRepo;
//
//    @BeforeEach
//    void setUp() {
//        shipmentDetailsRepo.deleteAllInBatch();
//        shipmentRepo.deleteAllInBatch();
//        driverRepo.deleteAllInBatch();
//        vehicleRepo.deleteAllInBatch();
//        userRepo.deleteAllInBatch();
//    }
//    @Test
//    void findByShipmentId() {
//        Optional<ShipmentDetail> found = shipmentDetailsRepo.findByShipmentId(createTestShipmentDetail().getShipment().getId());
//        assertTrue(found.isPresent());
//        assertEquals(createTestShipmentDetail().getArrivalTime(), found.get().getArrivalTime());
//    }
//
//    @Test
//    void findByArrivalTime() {
//        Optional<ShipmentDetail> found = shipmentDetailsRepo.findByArrivalTime(createTestShipmentDetail().getArrivalTime());
//        assertFalse(found.isEmpty());
//        assertEquals(createTestShipmentDetail().getArrivalTime(), found.get().getArrivalTime());
//    }
//
//


//    private ShipmentDetail createTestShipmentDetail() {
//        Driver driver = new Driver();
//        driver.setPhone("Phone Number Test");
//        driver.setName("Name Test");
//        driver.setLicenseNumber("License Number Test");
//        driver.setEmail(" Email Test");
//        driver.setPassword("Password Test");
//        driver.setRole("Role Test");
//        driver.setUsername("Username Test");
//
//        Address originAddress= new Address("Romania","Satu Mare","Grivitei","17A","5214");
//
//        Vehicle vehicle = vehicleRepo.save(new Vehicle("Vehicle Type", "REG123", 5000));
//
//        return shipmentDetailsRepo.save(new ShipmentDetail(shipment, driverRepo.save(driver), vehicle, LocalDateTime.of(2009,1,5,14,12,1), LocalDateTime.of(2009,1,5,14,12,1).plusDays(10)));
//    }

}