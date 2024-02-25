package com.mycode.pathpilotserver.customers.repository;

import com.mycode.pathpilotserver.customers.models.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {


    Optional<Customer> findByName(String name);





}
