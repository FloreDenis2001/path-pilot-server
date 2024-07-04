package com.mycode.pathpilotserver.driver.dto;


import com.mycode.pathpilotserver.driver.models.Driver;

import java.util.List;

public record DriverDTO(String username, String email, String firstName, String lastName, String phone, double salary,
                        double rating, int experience, String licenseNumber, boolean isAvailable) {
    public static List<DriverDTO> fromList(List<Driver> drivers) {
        return drivers.stream().map(DriverDTO::fromDriver).toList();
    }

    public static DriverDTO fromDriver(Driver driver) {
        return new DriverDTO(driver.getUsername(), driver.getEmail(), driver.getFirstName(), driver.getLastName(), driver.getPhone(), driver.getSalary(), driver.getRating(), driver.getExperience(), driver.getLicenseNumber(), driver.isAvailable());
    }


}
