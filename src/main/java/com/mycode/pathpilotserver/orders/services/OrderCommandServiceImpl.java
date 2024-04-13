package com.mycode.pathpilotserver.orders.services;


import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.orders.dto.OrderDTO;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepo orderRepo;
    private final UserRepo customerRepo;

    private final ShipmentRepo shipmentRepo;

    public OrderCommandServiceImpl(OrderRepo orderRepo ,UserRepo customerRepo, ShipmentRepo shipmentRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Optional<User> customer = customerRepo.findById(orderDTO.customerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer with id: " + orderDTO.customerId() + " not found");
        }

        Optional<Shipment> shipment = getShipments(orderDTO);
        Order order = getOrder(orderDTO,customer, shipment);

        shipmentRepo.saveAndFlush(shipment.get());
        orderRepo.saveAndFlush(order);
    }
    private static Optional<Shipment> getShipments(OrderDTO orderDTO) {
        Shipment shipment = new Shipment();
        shipment.setOriginName(orderDTO.originName());
        shipment.setDestinationName(orderDTO.destinationName());
        shipment.setOriginPhone(orderDTO.originPhone());
        shipment.setDestinationPhone(orderDTO.destinationPhone());
        shipment.setOriginAddress(orderDTO.origin());
        shipment.setDestinationAddress(orderDTO.destination());
        shipment.setStatus(StatusType.PICKED);
        shipment.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
        return Optional.of(shipment);
    }

    private static Order getOrder(OrderDTO orderDTO, Optional<User> customer, Optional<Shipment> shipment) {
        Order order = new Order();
        order.setCustomer((Customer) customer.get());
        order.setRoute(null);
        order.setShipment(shipment.get());
        order.setHeight(orderDTO.height());
        order.setWeight(orderDTO.weight());
        order.setType(orderDTO.type());
        order.setWidth(orderDTO.width());
        order.setDeliveryDescription(orderDTO.deliveryDescription());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(orderDTO.totalAmount());
        return order;
    }



}
