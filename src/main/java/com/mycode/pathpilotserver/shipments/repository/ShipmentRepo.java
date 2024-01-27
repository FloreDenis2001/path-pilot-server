package com.mycode.pathpilotserver.shipments.repository;

import com.mycode.pathpilotserver.shipments.models.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

    @EntityGraph(attributePaths={"shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Shipment findByShipmentNumber(String shipmentNumber);

    @EntityGraph(attributePaths={"shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Shipment findByOrderNumber(String orderNumber);

    @EntityGraph(attributePaths={"shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Shipment findByDriverName(String driverName);

    @EntityGraph(attributePaths={"shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Shipment findByCustomerName(String customerName);


}
