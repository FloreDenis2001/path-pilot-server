package com.mycode.pathpilotserver.packages.dto;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.packages.models.PackageType;
import com.mycode.pathpilotserver.system.enums.OrderType;

public record PackageDTO(Long customerId , double totalAmount,
                         double weight, double height, double width, String deliveryDescription,
                         String originName, String destinationName, String originPhone,
                         String destinationPhone, Address origin, Address destination) {
}