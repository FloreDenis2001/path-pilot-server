package com.mycode.pathpilotserver.vehicles.repository;

import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  VehicleRepo extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    Optional<List<Vehicle>> findByFuelType(FuelType fuelType);

    Optional<List<Vehicle>> findByCapacity(int capacity);

    @EntityGraph(attributePaths = {"company"},type = EntityGraph.EntityGraphType.LOAD)
    Optional<List<Vehicle>> getVehiclesByCompanyRegistrationNumber(String registrationNumber);

    @EntityGraph(attributePaths = {"company"},type = EntityGraph.EntityGraphType.LOAD)
    Optional<List<Vehicle>> getInactiveVehiclesByCompanyRegistrationNumber(String registrationNumber);






}
