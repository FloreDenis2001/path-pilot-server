package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;

import java.util.Optional;

public class UserQuerryServiceImpl implements UserQuerryService{

    private final UserRepo userRepo;

    public UserQuerryServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            return user;}
        else {
            throw new UserNotFoundException("User with email: " + email + " not found");
        }
    }


}
