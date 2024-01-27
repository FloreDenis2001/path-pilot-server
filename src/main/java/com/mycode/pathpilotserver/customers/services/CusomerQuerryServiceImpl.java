package com.mycode.pathpilotserver.customers.services;

import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CusomerQuerryServiceImpl implements CustomerQuerryService {

    private final CustomerRepo customerRepo;

    public CusomerQuerryServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            return customer;
        }else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByUserEmail(String email) {
        return Optional.empty();
    }
}
