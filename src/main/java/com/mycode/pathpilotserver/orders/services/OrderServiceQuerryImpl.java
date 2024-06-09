package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.exceptions.OrderNotFoundException;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceQuerryImpl implements OrderServiceQuerry {
    private final OrderRepo orderRepo;

    public OrderServiceQuerryImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }


    @Override
    public Optional<Order> findByCustomerAndAndShipment(Long customerId, Long shipmentId) {
        Optional<Order> order = orderRepo.findByCustomerAndAndShipment(customerId, shipmentId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order with customer id: " + customerId + " and shipment id: " + shipmentId + " not found");
        }
        return order;
    }


    @Override
    public Optional<List<Order>> findAllByCustomerEmail(String email) {
        Optional<List<Order>> orders = orderRepo.findAllByCustomerEmail(email);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Order with customer email: " + email + " not found");
        }
        return orders;
    }
}
