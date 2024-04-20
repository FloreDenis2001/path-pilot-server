package com.mycode.pathpilotserver.driver.dto;

public record DriverCreateRequest(String username,String email,String firstName,String lastName,String password,String phone,double salary,double rating,int experience, String licenseNumber) {
}
