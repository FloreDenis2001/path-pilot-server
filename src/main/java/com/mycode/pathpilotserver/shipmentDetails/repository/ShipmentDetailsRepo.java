package com.mycode.pathpilotserver.shipmentDetails.repository;


import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentDetailsRepo extends JpaRepository<ShipmentDetail, Long> {

    @EntityGraph(attributePaths={"driver","order"}, type = EntityGraph.EntityGraphType.LOAD)
    ShipmentDetail findByShipmentNumber(String shipmentNumber);

    @EntityGraph(attributePaths={"driver","order"}, type = EntityGraph.EntityGraphType.LOAD)
    ShipmentDetail findByDriverName(String driverName);

    @EntityGraph(attributePaths={"driver","order"}, type = EntityGraph.EntityGraphType.LOAD)
    ShipmentDetail findByOrderNumber(String orderNumber);





}
