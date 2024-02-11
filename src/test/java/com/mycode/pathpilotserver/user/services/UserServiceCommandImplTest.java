package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceCommandImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceCommandImpl userServiceCommand;


    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }
}