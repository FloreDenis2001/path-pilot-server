package com.mycode.pathpilotserver.customers.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
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


    @BeforeEach
    void setUp() {
        customerRepo.deleteAll();
    }
    
//    @Test
//    void



}