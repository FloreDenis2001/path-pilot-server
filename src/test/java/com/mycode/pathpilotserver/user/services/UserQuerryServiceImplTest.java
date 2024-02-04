package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserQuerryServiceImplTest {

    @Mock
    private UserRepo userRepo;

    private UserQuerryService userQuerryService;

    @BeforeEach
    void setUp() {
        userQuerryService = new UserQuerryServiceImpl(userRepo);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setPassword("password");
        return user;
    }

    @Test
    void findByEmail() {
        Optional<User> user = Optional.of(createTestUser());
        doReturn(user).when(userRepo).findByEmail("email");
        assertEquals(user, userQuerryService.findByEmail("email"));
    }

    @Test
    void findByEmailException(){
        doReturn(null).when(userRepo).findByEmail("email");
        assertThrows(NullPointerException.class, () -> userQuerryService.findByEmail("email"));
    }
}