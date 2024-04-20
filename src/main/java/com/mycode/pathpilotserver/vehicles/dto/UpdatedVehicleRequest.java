package com.mycode.pathpilotserver.vehicles.dto;


import com.mycode.pathpilotserver.vehicles.models.FuelType;

import java.time.LocalDate;

public record UpdatedVehicleRequest(String make, String model,
                                    int year , FuelType fuelType,
                                    double fuelCapacity , double fuelConsumption ,
                                    LocalDate lastService,LocalDate nextService ,
                                    double km ,  String registrationNumber , int capacity ,double width, double height, double length, double weight, boolean active) {
}

