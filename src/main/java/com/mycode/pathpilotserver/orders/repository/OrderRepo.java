package com.mycode.pathpilotserver.orders.repository;

import com.mycode.pathpilotserver.orders.models.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {


    @EntityGraph(attributePaths={"customer","driver"}, type = EntityGraph.EntityGraphType.LOAD)
    Order findByOrderNumber(String orderNumber);

    @EntityGraph(attributePaths={"customer","driver"}, type = EntityGraph.EntityGraphType.LOAD)
    Order findByCustomerName(String customerName);

    @EntityGraph(attributePaths={"customer","driver"}, type = EntityGraph.EntityGraphType.LOAD)
    Order findByDriverName(String driverName);

    @EntityGraph(attributePaths={"customer","driver"}, type = EntityGraph.EntityGraphType.LOAD)
    Order findByCustomerEmail(String customerEmail);

}
