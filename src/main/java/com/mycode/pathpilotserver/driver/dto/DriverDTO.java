package com.mycode.pathpilotserver.driver.dto;


import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.routes.models.Route;

import java.util.List;
import java.util.Set;

public record DriverDTO(String username, String email, String firstName, String lastName, String phone, double salary, double rating, int experience, String licenseNumber, boolean isAvailable) {
    public static List<DriverDTO> fromList(List<Driver> drivers) {
        return drivers.stream().map(DriverDTO::from).toList();
    }

    public static DriverDTO from(Driver driver) {
        return new DriverDTO(driver.getUsername(), driver.getEmail(), driver.getFirstName(), driver.getLastName(), driver.getPhone(), driver.getSalary(), driver.getRating(), driver.getExperience(), driver.getLicenseNumber(), driver.isAvailable());
    }
}
