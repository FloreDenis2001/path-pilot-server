package com.mycode.pathpilotserver.user.web;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.image.services.ImageServiceQuerryImpl;
import com.mycode.pathpilotserver.system.jwt.JWTTokenProvider;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class ServerControllerUser {

    private UserQuerryServiceImpl userQuerryServiceImpl;


    private UserServiceCommandImpl userServiceCommand;


    private AuthenticationManager authentificateManager ;

    private ImageServiceQuerryImpl imageServiceQuerryImpl;

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
        LoginResponse loginResponse = new LoginResponse(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName(),user.getRole(),user.getEmail(),user.getPhone(),user.getCompany().getRegistrationNumber(),AddressDTO.from(user.getAddress()),jwtHeader.getFirst(HttpHeaders.AUTHORIZATION));
        return new ResponseEntity<>(loginResponse, jwtHeader, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDTO registerDTO) {
        userServiceCommand.registerUser(registerDTO);
        User user = userQuerryServiceImpl.findByEmail(registerDTO.user().email()).get();
        HttpHeaders jwtHeader = getJwtHeader(user);
        RegisterResponse registerResponse = new RegisterResponse(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName(),user.getRole(),user.getEmail(),user.getPhone(),user.getCompany().getRegistrationNumber(),jwtHeader.getFirst(HttpHeaders.AUTHORIZATION));
        return new ResponseEntity<>(registerResponse, jwtHeader, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequest request) {
        userServiceCommand.deleteUser(request);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/reset/password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        userServiceCommand.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }


    @PostMapping("/update/image")
    public ResponseEntity<String> changeImage(@RequestParam("email") String email, @RequestParam("file") MultipartFile file) {
        userServiceCommand.uploadImage(file, email);
        String base64Image = imageServiceQuerryImpl.findImageByUserAfterEmail(email);
        return ResponseEntity.ok(base64Image);
    }

    @PutMapping("/update" )
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest request) {
        userServiceCommand.updateUser(request);
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }


}
