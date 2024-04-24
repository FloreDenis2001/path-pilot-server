package com.mycode.pathpilotserver.orders.dto;

import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.system.enums.OrderType;

public record OrderDTO(Long customerId, double totalAmount,
                       double weight, double height, double width, String deliveryDescription,
                       OrderType type, String originName,String destinationName,String originPhone,String destinationPhone, Address origin, Address destination) {
}
