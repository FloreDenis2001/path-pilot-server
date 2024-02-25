package com.mycode.pathpilotserver.customers.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
@Transactional
class CustomerRepoTest {


    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserRepo userRepo;


    @BeforeEach
    void setUp() {
        customerRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void findByName() {

        Address address= new Address("Romania","Satu Mare","Grivitei","17A","5214");

        Customer customer = new Customer();
        customer.setAddress(address);
        customer.setPhone("Phone Number Test");
        customer.setName("Name Test");
        customer.setEmail(" Email Test");
        customer.setPassword("Password Test");
        customer.setRole("Role Test");
        customer.setUsername("Username Test");
        customerRepo.saveAndFlush(customer);

        Customer customer1 = customerRepo.findByName("Name Test").get();
        assertEquals(customer1.getName(), customer.getName());
    }



}