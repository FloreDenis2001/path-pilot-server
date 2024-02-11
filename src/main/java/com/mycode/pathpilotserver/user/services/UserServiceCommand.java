package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.dto.LoginUserRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


public interface UserServiceCommand {

    void deleteUser(LoginUserRequest loginUserRequest);

    void updateUser(UpdateUserRequest updateUserRequest);



}
