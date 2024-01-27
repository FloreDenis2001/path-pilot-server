package com.mycode.pathpilotserver.user.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PathPilotServerApplication.class)
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
    }

    @Test
    void findByEmail() {
        User user = new User();
        user.setEmail(" Email Test");
        user.setPassword("Password Test");
        user.setRole("Role Test");
        user.setUsername("Username Test");
        user.setId(1L);
        userRepo.saveAndFlush(user);

        User user1 = userRepo.findByEmail(" Email Test");
        assertEquals(user1.getEmail(), user.getEmail());
    }
}