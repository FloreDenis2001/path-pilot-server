package com.mycode.pathpilotserver.user.web;

import com.mycode.pathpilotserver.system.jwt.JWTTokenProvider;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.dto.*;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.services.UserQuerryServiceImpl;
import com.mycode.pathpilotserver.user.services.UserServiceCommandImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class ServerControllerUser {

    private UserQuerryServiceImpl userQuerryServiceImpl;

    private UserServiceCommandImpl userServiceCommand;


    private AuthenticationManager authentificateManager ;

    private JWTTokenProvider jwtTokenProvider;

    private void authenticate(String email, String password) {
        authentificateManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private HttpHeaders getJwtHeader(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, jwtTokenProvider.generateJWTToken(user));
        return headers;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserRequest loginUserRequest) {
        authenticate(loginUserRequest.email(),loginUserRequest.password());
        User user = userQuerryServiceImpl.findByEmail(loginUserRequest.email()).get();
        HttpHeaders jwtHeader = getJwtHeader(user);
        LoginResponse loginResponse = new LoginResponse(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName(),user.getRole(),user.getEmail(),user.getPhone(),jwtHeader.getFirst(HttpHeaders.AUTHORIZATION));
        return new ResponseEntity<>(loginResponse, jwtHeader, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUserRequest registerDTO) {
        userServiceCommand.registerUser(registerDTO);
        User user = userQuerryServiceImpl.findByEmail(registerDTO.email()).get();
        HttpHeaders jwtHeader = getJwtHeader(user);
        RegisterResponse registerResponse = new RegisterResponse(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName(),user.getRole(),user.getEmail(),user.getPhone(),jwtHeader.getFirst(HttpHeaders.AUTHORIZATION));
        return new ResponseEntity<>(registerResponse, jwtHeader, HttpStatus.OK);
    }

}