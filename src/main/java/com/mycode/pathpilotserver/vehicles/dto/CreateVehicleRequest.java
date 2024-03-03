package com.mycode.pathpilotserver.vehicles.dto;

public record CreateVehicleRequest(String registrationNumber, String type, int capacity) {
}
