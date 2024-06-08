package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.models.Order;

import java.util.Optional;

import java.util.List;

public interface OrderServiceQuerry {

    Optional<Order> findByCustomerAndAndShipment(Long customerId, Long shipmentId);

    Optional<List<Order>> findAllByCustomerEmail(String email);
}
