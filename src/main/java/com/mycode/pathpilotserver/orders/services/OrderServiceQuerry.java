package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.models.Order;

import java.util.Optional;

public interface OrderServiceQuerry {

    Optional<Order> findByCustomerName(String customerName);
}