package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
public interface VehicleServiceQuerry {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
}
