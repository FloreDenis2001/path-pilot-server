package com.mycode.pathpilotserver.shipmentDetails.repository;


import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShipmentDetailsRepo extends JpaRepository<ShipmentDetail, Long> {




}

