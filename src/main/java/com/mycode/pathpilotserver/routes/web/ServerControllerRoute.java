package com.mycode.pathpilotserver.routes.web;


import com.mycode.pathpilotserver.routes.services.RouteServiceCommandImpl;
import com.mycode.pathpilotserver.user.dto.LoginResponse;
import com.mycode.pathpilotserver.user.dto.LoginUserRequest;
import com.mycode.pathpilotserver.user.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/route")
@AllArgsConstructor
@Slf4j
public class ServerControllerRoute {

    private final RouteServiceCommandImpl routeServiceCommand;


    @PostMapping("/generateRoute")
    public ResponseEntity<String> generateRoute(@RequestParam String companyRegistrationNumber) {
        routeServiceCommand.generateRoute(companyRegistrationNumber);
        return ResponseEntity.ok("Route generated for company with registration number: " + companyRegistrationNumber);
    }
}
