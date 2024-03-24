package com.mycode.pathpilotserver.shipmentDetails.repository;


import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentDetailsRepo extends JpaRepository<ShipmentDetail, Long> {




}

