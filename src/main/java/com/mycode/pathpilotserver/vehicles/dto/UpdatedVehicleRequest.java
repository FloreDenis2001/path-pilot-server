package com.mycode.pathpilotserver.vehicles.dto;


public record UpdatedVehicleRequest(String registrationNumber, String type, int capacity) {
}

