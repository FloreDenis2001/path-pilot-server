package com.mycode.pathpilotserver.customers.services;

import com.mycode.pathpilotserver.customers.dto.CustomerCreateRequest;
import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import com.mycode.pathpilotserver.customers.exceptions.CustomerAlreadyExist;
import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class CustomerCommandServiceImpl implements CustomerCommandService {

    private final CustomerRepo customerRepo;

    private final UserRepo userRepo;

    public CustomerCommandServiceImpl(CustomerRepo customerRepo, UserRepo userRepo) {
        this.customerRepo = customerRepo;
        this.userRepo = userRepo;
    }


    @Override
    public void createCustomer(CustomerCreateRequest customerCreateRequest) {
        Optional<User> user = userRepo.findByEmail(customerCreateRequest.email());
        if (user.isEmpty()) {
            Customer customer = new Customer();
            customer.setEmail(customerCreateRequest.email());
            customer.setPhone(customerCreateRequest.phone());
            customer.setPassword(customerCreateRequest.password());
            customer.setRole("CUSTOMER");
            customer.setAddress(customerCreateRequest.address());
            customer.setUsername(customerCreateRequest.username());
            customerRepo.saveAndFlush(customer);
        } else {
            throw new CustomerAlreadyExist("Customer already exists ! ");
        }
    }

    @Override
    public void deleteCustomer(RemoveValidationRequest removeValidationRequest) {
        Optional<User> user = userRepo.findByEmail(removeValidationRequest.email());
        if(user.isPresent() && user.get().getPassword().equals(removeValidationRequest.password()) && user.get().getRole().equals("CUSTOMER")) {
            customerRepo.delete((Customer) user.get());
        }else {
            throw new CustomerNotFoundException("Customer with email " + removeValidationRequest.email() + " not found !");
        }
    }
}
