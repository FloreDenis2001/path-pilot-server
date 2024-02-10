package com.mycode.pathpilotserver.user.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface UserServiceCommand {

    void deleteUser(String email, String password);

    void updateUser(String email, String password);



}
