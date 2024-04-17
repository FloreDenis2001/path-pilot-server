package com.mycode.pathpilotserver.vehicles.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
@Transactional
class VehicleRepoTest {


    @Autowired
    private  VehicleRepo vehicleRepo;



    @BeforeEach
    void setUp() {
        vehicleRepo.deleteAll();
    }

//    public Vehicle createVehicle() {
//        Vehicle vehicle = Vehicle.builder().registrationNumber("KAA 001A").capacity(2000).type("Coupe").build();
//        return vehicleRepo.save(vehicle);
//    }

//    @Test
//    void findByRegistrationNumber() {
//        Optional<Vehicle> vehicle = vehicleRepo.findByRegistrationNumber(createVehicle().getRegistrationNumber());
//        assertEquals(createVehicle().getRegistrationNumber(), vehicle.get().getRegistrationNumber());
//    }
}