package com.mycode.pathpilotserver.shipments.repository;

import com.mycode.pathpilotserver.shipments.models.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

//    Shipment findByShipmentNumber(String shipmentNumber);
//
//    Shipment findByOrderNumber(String orderNumber);
//
//    Shipment findByDriverName(String driverName);
//
//    Shipment findByCustomerName(String customerName);


}
