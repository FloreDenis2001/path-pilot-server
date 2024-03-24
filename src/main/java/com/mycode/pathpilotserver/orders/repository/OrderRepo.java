package com.mycode.pathpilotserver.orders.repository;

import com.mycode.pathpilotserver.orders.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.shipment.id = :shipmentId")
    Optional<Order> findByCustomerAndAndShipment(Long customerId, Long shipmentId);







}
