package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.exceptions.OrderNotFoundException;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;

import java.util.Optional;

public class OrderServiceQuerryImpl implements OrderServiceQuerry {
    private final OrderRepo orderRepo;

    public OrderServiceQuerryImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public Optional<Order> findByCustomerName(String customerName) {
        Optional<Order> order = orderRepo.findByCustomerName(customerName);
        if (order.isPresent()) {
            return order;
        } else {
            throw new OrderNotFoundException("Order with customer name: " + customerName + " not found");
        }
    }
}
