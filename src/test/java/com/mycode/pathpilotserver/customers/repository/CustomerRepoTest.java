package com.mycode.pathpilotserver.customers.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
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
        User user = new User();
        user.setEmail(" Email Test");
        user.setPassword("Password Test");
        user.setRole("Role Test");
        user.setUsername("Username Test");
        user.setId(1L);
        userRepo.saveAndFlush(user);


        Customer customer = new Customer();
        customer.setId(1L);
        customer.setAddress("Address Test");
        customer.setPhone("Phone Number Test");
        customer.setUser(user);
        customer.setName("Name Test");
        customerRepo.saveAndFlush(customer);

        Customer customer1 = customerRepo.findByName("Name Test");
        assertEquals(customer1.getName(), customer.getName());
    }

    @Test
    void findByUserEmail() {

        User user = new User();
        user.setEmail(" Email Test");
        user.setPassword("Password Test");
        user.setRole("Role Test");
        user.setUsername("Username Test");
        user.setId(1L);
        userRepo.saveAndFlush(user);


        Customer customer = new Customer();
        customer.setId(1L);
        customer.setAddress("Address Test");
        customer.setPhone("Phone Number Test");
        customer.setUser(user);
        customer.setName("Name Test");
        customerRepo.saveAndFlush(customer);

        Customer customer1 = customerRepo.findByUserEmail(" Email Test");
        assertEquals(customer1.getUser().getEmail(), customer.getUser().getEmail());
    }


}