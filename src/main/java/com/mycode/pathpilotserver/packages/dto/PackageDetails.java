package com.mycode.pathpilotserver.packages.dto;

import lombok.Builder;

@Builder
public record PackageDetails(double totalAmount, double weight, double height, double length, double width,String deliveryDescription) {
}
