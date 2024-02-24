package com.mycode.pathpilotserver.customers.services;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class CustomerQuerryServiceImplTest {

    @Mock
    private CustomerRepo customerRepo;


    private CustomerQuerryService customerQuerryService;

    @BeforeEach
    void setUp() {
        customerQuerryService= new CustomerQuerryServiceImpl(customerRepo);
    }

    private Customer createCustomer() {
        Address address= new Address("Romania","Satu Mare","Grivitei","17A","5214");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setPhone("Phone Number Test");
        customer.setCustomerAddress(address);
        customer.setName("Name Test");
        return customer;
    }

    @Test
    void findById() {
        Optional<Customer> customer = Optional.of(createCustomer());
        doReturn(customer).when(customerRepo).findById(1L);
        assertEquals(customer.get(), customerQuerryService.findById(1L).get());
    }

    @Test
    void findByIdException(){
        doReturn(Optional.empty()).when(customerRepo).findById(1L);
        assertThrows(CustomerNotFoundException.class, () -> customerQuerryService.findById(1L).get());
    }

    @Test
    void findByName() {
        Optional<Customer> customer = Optional.of(createCustomer());
        doReturn(customer).when(customerRepo).findByName("Name Test");
        assertEquals(customer.get(), customerQuerryService.findByName("Name Test").get());
    }

    @Test
    void findByNameException(){
        doReturn(Optional.empty()).when(customerRepo).findByName("Name Test");
        assertThrows(CustomerNotFoundException.class, () -> customerQuerryService.findByName("Name Test").get());
    }

}