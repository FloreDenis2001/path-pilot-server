package com.mycode.pathpilotserver.routes.dto;

import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import lombok.Builder;

import java.util.Set;

@Builder
public record RouteDTO(Long id, double totalDistance, Vehicle vehicle, DriverDTO driver , Set<Order> orders) {
}
