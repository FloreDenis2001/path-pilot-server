package com.mycode.pathpilotserver.shipments.services;

import com.mycode.pathpilotserver.address.Address;
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
        Address originAddress= new Address("Romania","Satu Mare","Grivitei","17A","5214");
        Address destinationAddress = new Address("Romania","Bucuresti","Dambovicioarei","17","99921");
        Shipment shipment = new Shipment();
        shipment.setId(1L);
        shipment.setDestinationAddress(destinationAddress);
        shipment.setOriginAddress(originAddress);
        shipment.setEstimatedDeliveryDate(LocalDateTime.of(2024,12,12,14,20,0));
        return shipment;
    }

    @Test
    void findByOrigin() {
        Optional<Shipment> shipment = Optional.of(createShipment());
        Address originAddress= new Address("Romania","Satu Mare","Grivitei","17A","5214");

        doReturn(shipment).when(shipmentRepo).findByOrigin(originAddress);
        assertEquals(shipment, shipmentServiceQuerry.findByOrigin(originAddress));
    }

    @Test
    void findByOriginException(){
        Address originAddress = new Address("Romania","Bucuresti","Dambovicioarei","17","99921");

        doReturn(null).when(shipmentRepo).findByOrigin(originAddress);
        assertThrows(NullPointerException.class, () -> shipmentServiceQuerry.findByOrigin(originAddress));
    }
}