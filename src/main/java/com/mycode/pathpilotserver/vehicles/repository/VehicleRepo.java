package com.mycode.pathpilotserver.vehicles.repository;

import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    Optional<List<Vehicle>> findByType(String type);

    Optional<List<Vehicle>> findByCapacity(int capacity);

}
