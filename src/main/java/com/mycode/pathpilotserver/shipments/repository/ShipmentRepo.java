package com.mycode.pathpilotserver.shipments.repository;

import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

}
