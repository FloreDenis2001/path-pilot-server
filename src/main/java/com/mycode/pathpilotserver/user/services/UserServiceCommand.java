package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.dto.*;
import org.springframework.web.multipart.MultipartFile;


public interface UserServiceCommand {

    void deleteUser(LoginUserRequest loginUserRequest);

    void updateUser(UpdateUserRequest updateUserRequest);

    String uploadImage(MultipartFile file,String email);



    void registerUser(RegisterDTO registerDTO);



}
