package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderServiceQuerry {

    Optional<Order> findByCustomerAndAndShipment(Long customerId, Long shipmentId);
}
