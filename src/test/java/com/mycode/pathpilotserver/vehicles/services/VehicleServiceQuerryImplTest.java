package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class VehicleServiceQuerryImplTest {

    @Mock
    private VehicleRepo vehicleRepo;

    private VehicleServiceQuerry vehicleServiceQuerry;

    @BeforeEach
    void setUp() {
        vehicleServiceQuerry = new VehicleServiceQuerryImpl(vehicleRepo);
    }

//    public Vehicle createVehicle() {
//        Vehicle vehicle = Vehicle.builder().registrationNumber("KAA 001A").capacity(2000).type("Coupe").build();
//        return vehicle;
//    }
//
//    @Test
//    void findByRegistrationNumber() {
//        Optional<Vehicle> vehicle = Optional.of(createVehicle());
//        doReturn(vehicle).when(vehicleRepo).findByRegistrationNumber("KAA 001A");
//        assertEquals(vehicle, vehicleServiceQuerry.findByRegistrationNumber("KAA 001A"));
//    }
//
//    @Test
//    void findByRegistrationNumberException(){
//        doReturn(null).when(vehicleRepo).findByRegistrationNumber("KAA 001A");
//        assertThrows(NullPointerException.class, () -> vehicleServiceQuerry.findByRegistrationNumber("KAA 001A"));
//    }
}