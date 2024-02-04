package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.exceptions.OrderNotFoundException;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceQuerryImplTest {

    @Mock
    private OrderRepo orderRepo;

    private OrderServiceQuerry orderServiceQuerry;

    @BeforeEach
    void setUp() {
        orderServiceQuerry = new OrderServiceQuerryImpl(orderRepo);
    }

    public Order createOrder(){
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(200.98);
        order.setOrderDate(LocalDateTime.of(2021, 10, 10, 10, 10));
        return order;
    }

    @Test
    void findByCustomerName() {
        Optional<Order> order = Optional.of(createOrder());
        doReturn(order).when(orderRepo).findByCustomerName("Customer Name");
        assertEquals(order.get(), orderServiceQuerry.findByCustomerName("Customer Name").get());
    }

    @Test
    void findByCustomerNameException(){
        doReturn(Optional.empty()).when(orderRepo).findByCustomerName("Customer Name");
        assertThrows(OrderNotFoundException.class, () -> orderServiceQuerry.findByCustomerName("Customer Name").get());
    }




}