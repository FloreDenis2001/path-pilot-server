package com.mycode.pathpilotserver.driver.dto;

public record DriverUpdateRequest(String username, String email, String firstName, String lastName, String phone,
                                  double salary, double rating, int experience, String licenseNumber,
                                  boolean isAvailable , String companyRegistrationNumber) {
}
