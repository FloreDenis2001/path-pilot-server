package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.dto.DeleteUserRequest;
import com.mycode.pathpilotserver.user.dto.RegisterDTO;
import com.mycode.pathpilotserver.user.dto.ResetPasswordRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;


public interface UserServiceCommand {

    void deleteUser(DeleteUserRequest deleteUserRequest);

    void updateUser(UpdateUserRequest updateUserRequest);

    String uploadImage(MultipartFile file,String email);

    void registerUser(RegisterDTO registerDTO);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);



}
