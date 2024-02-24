package com.mycode.pathpilotserver.shipments.repository;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {


    @EntityGraph(attributePaths = {"orders","shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select s from Shipments s  where s.originAddress=?1")
    Optional<Shipment> findByOrigin(Address originAddress);


}
