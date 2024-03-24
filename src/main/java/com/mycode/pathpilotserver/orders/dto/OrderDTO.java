package com.mycode.pathpilotserver.orders.dto;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.system.enums.OrderType;

public record OrderDTO(Long customerId, double totalAmount,
                       double weight, double height, double width, double volume, String deliveryDescription,
                       OrderType type, Address origin, Address destination) {
}
