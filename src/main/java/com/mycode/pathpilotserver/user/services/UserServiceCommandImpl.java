package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.dto.LoginUserRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.exceptions.WrongPasswordException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceCommandImpl implements UserServiceCommand {

    private final UserRepo userRepo;

    public UserServiceCommandImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void deleteUser(LoginUserRequest loginUserRequest) {
        Optional<User> user = userRepo.findByEmail(loginUserRequest.email());
        validatePassword(user.get(), loginUserRequest.password());
        user.ifPresentOrElse(userRepo::delete, () -> {
            throw new UserNotFoundException("User not found for email: " + loginUserRequest.email());
        });
    }

    @Override
    public void updateUser(UpdateUserRequest request) {
        User user = findUserByEmail(request.email());
        validatePassword(user, request.password());
        applyNewDetailsToUser(user, request.newUser());
        userRepo.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
    }

    private void validatePassword(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new WrongPasswordException("Invalid password for user: " + user.getEmail());
        }
    }

    private void applyNewDetailsToUser(User user, User newUserDetails) {
        user.setEmail(newUserDetails.getEmail());
        user.setPassword(newUserDetails.getPassword());
        user.setUsername(newUserDetails.getUsername());
    }

}
