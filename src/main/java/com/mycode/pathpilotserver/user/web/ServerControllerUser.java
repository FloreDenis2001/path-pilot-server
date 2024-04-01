package com.mycode.pathpilotserver.user.web;

import com.mycode.pathpilotserver.system.jwt.JWTTokenProvider;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.dto.LoginResponse;
import com.mycode.pathpilotserver.user.dto.RegisterResponse;
import com.mycode.pathpilotserver.user.dto.UserDTO;
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
    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO userDTO) {
        authenticate(userDTO.email(), userDTO.password());
        User loginUser = userQuerryServiceImpl.findByEmail(userDTO.email()).get();
        HttpHeaders jwtHeader = getJwtHeader(loginUser);
        LoginResponse loginResponse = new LoginResponse(loginUser.getId(), loginUser.getEmail(), jwtHeader.getFirst(HttpHeaders.AUTHORIZATION), loginUser.getUsername(), loginUser.getRole().name(), UserRole.valueOf(loginUser.getRole().name()));
        return new ResponseEntity<>(loginResponse, jwtHeader, HttpStatus.OK);
    }



//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping("/register")
//    public ResponseEntity<RegisterResponse> addUser(@RequestBody UserDTO userDTO) {
//    }

}
