package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;

import java.util.List;
import java.util.Optional;


public interface VehicleServiceQuerry {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    Optional<List<Vehicle>> findByFuelType(FuelType fuelType);


    Optional<List<Vehicle>> findByCapacity(int capacity);

    Optional<List<Vehicle>> findAll();


    Optional<List<Vehicle>> getVehiclesByCompanyRegistrationNumber(String registrationNumber);



}
