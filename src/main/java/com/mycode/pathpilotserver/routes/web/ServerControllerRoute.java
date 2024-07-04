package com.mycode.pathpilotserver.routes.web;


import com.mycode.pathpilotserver.routes.dto.RouteDTO;
import com.mycode.pathpilotserver.routes.services.RouteServiceCommandImpl;
import com.mycode.pathpilotserver.routes.services.RouteServiceQuerryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/route")
@AllArgsConstructor
@Slf4j
public class ServerControllerRoute {

    private final RouteServiceCommandImpl routeServiceCommand;

    private final RouteServiceQuerryImpl routeServiceQuerry;


    @PostMapping("/generateRoute")
    public ResponseEntity<String> generateRoute(@RequestParam String companyRegistrationNumber) throws IOException {
        routeServiceCommand.generateRoute(companyRegistrationNumber);
        return ResponseEntity.ok("Route generated for company with registration number: " + companyRegistrationNumber);
    }


    @GetMapping("/findAllByCompanyRegistrationNumber={companyRegistrationNumber}")
    public ResponseEntity<List<RouteDTO>> findAllByCompanyRegistrationNumber(@PathVariable String companyRegistrationNumber) {
        return ResponseEntity.ok(routeServiceQuerry.findAllByCompanyRegistrationNumber(companyRegistrationNumber).get());
    }

}
