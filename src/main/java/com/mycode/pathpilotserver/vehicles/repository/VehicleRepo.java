package com.mycode.pathpilotserver.vehicles.repository;

import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Long> {

    @EntityGraph(attributePaths={"shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Vehicle findByVehicleNumber(String vehicleNumber);



}
