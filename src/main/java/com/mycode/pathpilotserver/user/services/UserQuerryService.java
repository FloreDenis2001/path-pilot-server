package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.user.models.User;

import java.util.Optional;

public interface UserQuerryService {

    Optional<User> findByEmail(String email);

}
