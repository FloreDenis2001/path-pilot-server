package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.orders.dto.OrderDTO;

public interface OrderCommandService {
    void createOrder(OrderDTO orderDTO);
}
