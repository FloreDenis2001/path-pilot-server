package com.mycode.pathpilotserver.orders.services;


import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import com.mycode.pathpilotserver.orders.dto.OrderDTO;
import com.mycode.pathpilotserver.orders.exceptions.OrderNotFoundException;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;

    private final ShipmentRepo shipmentRepo;

    public OrderCommandServiceImpl(OrderRepo orderRepo, CustomerRepo customerRepo, ShipmentRepo shipmentRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Optional<Customer> customer = customerRepo.findById(orderDTO.customerId());
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer with id: " + orderDTO.customerId() + " not found");
        }

        Optional<Shipment> shipment = getShipments(orderDTO);
        Order order = getOrder(orderDTO, customer, shipment);
        shipmentRepo.saveAndFlush(shipment.get());
        orderRepo.saveAndFlush(order);

    }

    private static Optional<Shipment> getShipments(OrderDTO orderDTO) {
        Shipment shipment = new Shipment();
        shipment.setOriginAddress(orderDTO.origin());
        shipment.setDestinationAddress(orderDTO.destination());
        shipment.setStatus("Pending");
        shipment.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
        return Optional.of(shipment);
    }

    private static Order getOrder(OrderDTO orderDTO, Optional<Customer> customer, Optional<Shipment> shipment) {
        Order order = new Order();
        order.setCustomer(customer.get());
        order.setShipment(shipment.get());
        order.setHeight(orderDTO.height());
        order.setWeight(orderDTO.weight());
        order.setType(orderDTO.type());
        order.setVolume(orderDTO.volume());
        order.setWidth(orderDTO.width());
        order.setDeliveryDescription(orderDTO.deliveryDescription());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(orderDTO.totalAmount());
        return order;
    }

    @Override
    public void updateOrder(OrderDTO orderDTO) {
//        Optional<Order> order = orderRepo.findByCustomerAndAndShipment(orderDTO.customerId());
//
//
//        if (order.isEmpty()) {
//            throw new OrderNotFoundException("Order with customer id: " + orderDTO.customerId() + " and shipment id: " + orderDTO.shipmentId() + " not found");
//        } else {
//            shipment.get().setOriginAddress(orderDTO.origin());
//            shipment.get().setDestinationAddress(orderDTO.destination());
//            shipment.get().setStatus("Pending");
//            shipment.get().setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
//            shipmentRepo.saveAndFlush(shipment.get());
//
//            order.get().setHeight(orderDTO.height());
//            order.get().setWeight(orderDTO.weight());
//            order.get().setType(orderDTO.type());
//            order.get().setVolume(orderDTO.volume());
//            order.get().setWidth(orderDTO.width());
//            order.get().setDeliveryDescription(orderDTO.deliveryDescription());
//            order.get().setOrderDate(orderDTO.orderDate());
//            order.get().setTotalAmount(orderDTO.totalAmount());
//            orderRepo.saveAndFlush(order.get());
//        }
    }

    @Override
    public void deleteOrder(Long customerId, Long shipmentId) {
        Optional<Order> order = orderRepo.findByCustomerAndAndShipment(customerId, shipmentId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order with customer id: " + customerId + " and shipment id: " + shipmentId + " not found");
        } else {
            orderRepo.delete(order.get());
        }
    }
}
