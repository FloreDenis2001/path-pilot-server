package com.mycode.pathpilotserver.driver.dto;

public record DriverCreateRequest(String name,String licenseNumber,String phone, String password, String username, String email) {
}
