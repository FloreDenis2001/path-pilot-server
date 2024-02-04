package com.mycode.pathpilotserver.shipmentDetails.services;

import com.mycode.pathpilotserver.shipmentDetails.exceptions.ShipmentDetailsNotFoundException;
import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import com.mycode.pathpilotserver.shipmentDetails.repository.ShipmentDetailsRepo;
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
class ShipmentDetailsServiceQuerryImplTest {

    @Mock
    private ShipmentDetailsRepo shipmentDetailsRepo;

    private ShipmentDetailsServiceQuerry shipmentDetailsServiceQuerry;

    @BeforeEach
    void setUp() {
        shipmentDetailsServiceQuerry = new ShipmentDetailsServiceQuerryImpl(shipmentDetailsRepo);
    }

    private ShipmentDetail createShipmentDetails() {
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setId(1L);
        shipmentDetail.setArrivalTime(LocalDateTime.of(2021, 10, 10, 10, 10));
        return shipmentDetail;
    }

    @Test
    void findByShipmentId() {
        Optional<ShipmentDetail> shipmentDetail = Optional.of(createShipmentDetails());
        doReturn(shipmentDetail).when(shipmentDetailsRepo).findByShipmentId(1L);
        assertEquals(shipmentDetail.get(), shipmentDetailsServiceQuerry.findByShipmentId(1L).get());
    }

    @Test
    void findByShipmentIdException(){
        doReturn(Optional.empty()).when(shipmentDetailsRepo).findByShipmentId(1L);
        assertThrows(ShipmentDetailsNotFoundException.class, () -> shipmentDetailsServiceQuerry.findByShipmentId(1L).get());
    }

    @Test
    void findByArrivalTime() {
        Optional<ShipmentDetail> shipmentDetail = Optional.of(createShipmentDetails());
        doReturn(shipmentDetail).when(shipmentDetailsRepo).findByArrivalTime(LocalDateTime.of(2021, 10, 10, 10, 10));
        assertEquals(shipmentDetail.get(), shipmentDetailsServiceQuerry.findByArrivalTime(LocalDateTime.of(2021, 10, 10, 10, 10)).get());
    }

    @Test
    void findByArrivalTimeException(){
        doReturn(Optional.empty()).when(shipmentDetailsRepo).findByArrivalTime(LocalDateTime.of(2021, 10, 10, 10, 10));
        assertThrows(ShipmentDetailsNotFoundException.class, () -> shipmentDetailsServiceQuerry.findByArrivalTime(LocalDateTime.of(2021, 10, 10, 10, 10)).get());
    }
}