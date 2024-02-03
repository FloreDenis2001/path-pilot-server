package com.mycode.pathpilotserver.orders.repository;

import com.mycode.pathpilotserver.orders.models.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {


    @EntityGraph(attributePaths = {"customer","driver"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findByCustomerName(String customerName);

}
