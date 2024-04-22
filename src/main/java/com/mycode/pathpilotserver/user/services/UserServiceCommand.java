package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.dto.LoginUserRequest;
import com.mycode.pathpilotserver.user.dto.RegisterDTO;
import com.mycode.pathpilotserver.user.dto.RegisterUserRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;


public interface UserServiceCommand {

    void deleteUser(LoginUserRequest loginUserRequest);

    void updateUser(UpdateUserRequest updateUserRequest);


    void registerUser(RegisterDTO registerDTO);



}
