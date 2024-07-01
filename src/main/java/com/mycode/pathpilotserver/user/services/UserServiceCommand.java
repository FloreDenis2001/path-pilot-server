package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.dto.DeleteUserRequest;
import com.mycode.pathpilotserver.user.dto.RegisterDTO;
import com.mycode.pathpilotserver.user.dto.ResetPasswordRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UserServiceCommand {

    void deleteUser(DeleteUserRequest deleteUserRequest);


    String uploadImage(MultipartFile file,String email);

    void registerUser(RegisterDTO registerDTO) throws IOException;

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    void updateUser(UpdateUserRequest request);


}
