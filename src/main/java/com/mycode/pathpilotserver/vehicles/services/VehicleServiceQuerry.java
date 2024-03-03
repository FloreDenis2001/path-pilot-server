package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface VehicleServiceQuerry {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    Optional<List<Vehicle>> findByType(String type);

    Optional<List<Vehicle>> findByCapacity(int capacity);
}
