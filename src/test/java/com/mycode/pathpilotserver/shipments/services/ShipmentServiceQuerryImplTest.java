package com.mycode.pathpilotserver.shipments.services;

import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceQuerryImplTest {

    @Mock
    private ShipmentRepo shipmentRepo;

    private ShipmentServiceQuerry shipmentServiceQuerry;

    @BeforeEach
    void setUp() {
        shipmentServiceQuerry = new ShipmentServiceQuerryImpl(shipmentRepo);
    }

    public Shipment createShipment(){
        Shipment shipment = new Shipment();
        shipment.setId(1L);
        shipment.setDestination("Destination");
        shipment.setOrigin("Origin");
        shipment.setEstimatedDeliveryDate(LocalDateTime.of(2024,12,12,14,20,0));
        return shipment;
    }

    @Test
    void findByOrigin() {
        Optional<Shipment> shipment = Optional.of(createShipment());
        doReturn(shipment).when(shipmentRepo).findByOrigin("Origin");
        assertEquals(shipment, shipmentServiceQuerry.findByOrigin("Origin"));
    }

    @Test
    void findByOriginException(){
        doReturn(null).when(shipmentRepo).findByOrigin("Origin");
        assertThrows(NullPointerException.class, () -> shipmentServiceQuerry.findByOrigin("Origin"));
    }
}