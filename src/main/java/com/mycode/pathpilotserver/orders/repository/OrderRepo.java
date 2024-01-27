package com.mycode.pathpilotserver.orders.repository;

import com.mycode.pathpilotserver.orders.models.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {

//
//    Order findByOrderNumber(String orderNumber);
//
//    Order findByCustomerName(String customerName);
//
//    Order findByDriverName(String driverName);
//
//    Order findByCustomerEmail(String customerEmail);

}
