package com.mycode.pathpilotserver.customers.services;

import com.mycode.pathpilotserver.customers.models.Customer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustomerQuerryService {

    Optional<Customer> findById(Long id);

    Optional<Customer> findByName(String name);

    Optional<Customer> findByUserEmail(String email);

}
