package com.mycode.pathpilotserver.customers.repository;

import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.orders.models.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths={"user","orders"}, type = EntityGraph.EntityGraphType.LOAD)
    Customer findByName(String name);

    @EntityGraph(attributePaths={"user","orders"}, type = EntityGraph.EntityGraphType.LOAD)
    Customer findByUserEmail(String email);

}
