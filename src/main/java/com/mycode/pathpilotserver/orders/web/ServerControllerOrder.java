package com.mycode.pathpilotserver.orders.web;


import com.mycode.pathpilotserver.orders.dto.OrderDTO;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.services.OrderCommandServiceImpl;
import com.mycode.pathpilotserver.orders.services.OrderServiceQuerryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@CrossOrigin
public class ServerControllerOrder {

    private final OrderServiceQuerryImpl orderServiceQuerry;

    private final OrderCommandServiceImpl orderServiceCommand;

    public ServerControllerOrder(OrderServiceQuerryImpl orderServiceQuerry, OrderCommandServiceImpl orderServiceCommand) {
        this.orderServiceQuerry = orderServiceQuerry;
        this.orderServiceCommand = orderServiceCommand;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByCustomerAndShipment={customerId},{shipmentId}")
    public ResponseEntity<Order> findByCustomerAndAndShipment(@PathVariable Long customerId, @PathVariable Long shipmentId) {
        return ResponseEntity.ok(orderServiceQuerry.findByCustomerAndAndShipment(customerId, shipmentId).get());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestBody OrderDTO order) {
        orderServiceCommand.createOrder(order);
        return ResponseEntity.ok().build();
    }








}
