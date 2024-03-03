package com.mycode.pathpilotserver.customers.web;

import com.mycode.pathpilotserver.customers.dto.CustomerCreateRequest;
import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.services.CustomerCommandServiceImpl;
import com.mycode.pathpilotserver.customers.services.CustomerQuerryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@Slf4j
@CrossOrigin
public class ServerControllerCustomer {

    private final CustomerCommandServiceImpl customerCommandService;

    private final CustomerQuerryServiceImpl customerQuerryService;

    public ServerControllerCustomer(CustomerCommandServiceImpl customerCommandService, CustomerQuerryServiceImpl customerQuerryService) {
        this.customerCommandService = customerCommandService;
        this.customerQuerryService = customerQuerryService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByName={name}")
    public ResponseEntity<Customer> findByName(@PathVariable String name) {
        return ResponseEntity.ok(customerQuerryService.findByName(name).get());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findById={id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerQuerryService.findById(id).get());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerCreateRequest customer) {
        customerCommandService.create(customer);
        return ResponseEntity.ok("Customer added successfully");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCustomer(@RequestBody RemoveValidationRequest removeValidationRequest) {
        customerCommandService.delete(removeValidationRequest);
        return ResponseEntity.ok("Customer deleted successfully");
    }



}
