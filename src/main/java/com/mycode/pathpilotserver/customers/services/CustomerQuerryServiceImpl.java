package com.mycode.pathpilotserver.customers.services;

import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerQuerryServiceImpl implements CustomerQuerryService {

    private final CustomerRepo customerRepo;

    public CustomerQuerryServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            return customer;
        }else {
            throw new CustomerNotFoundException("Customer with id: " + id + " not found");
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        Optional<Customer> customer = customerRepo.findByName(name);
        if (customer.isPresent()) {
            return customer;
        }else {
            throw new CustomerNotFoundException("Customer with name: " + name + " not found");
        }
    }

    @Override
    public Optional<Customer> findByUserEmail(String email) {
        Optional<Customer> customer = customerRepo.findByUserEmail(email);
        if (customer.isPresent()) {
            return customer;
        }else {
            throw new CustomerNotFoundException("Customer with email: " + email + " not found");
        }
    }
}
