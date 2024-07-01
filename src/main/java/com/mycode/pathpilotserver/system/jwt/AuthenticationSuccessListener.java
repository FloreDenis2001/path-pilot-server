package com.mycode.pathpilotserver.system.jwt;

import com.mycode.pathpilotserver.system.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    private final LoginAttemptService loginAttemptService;
    @Autowired
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails username = (UserDetails) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(username.getUsername());
        }
    }

}