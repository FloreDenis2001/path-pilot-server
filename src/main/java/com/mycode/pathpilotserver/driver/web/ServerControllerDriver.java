package com.mycode.pathpilotserver.driver.web;

import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;
import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.dto.DriverUpdateRequest;
import com.mycode.pathpilotserver.driver.services.DriverCommandServiceImpl;
import com.mycode.pathpilotserver.driver.services.DriverServiceQuerryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/drivers")
@Slf4j
@CrossOrigin
public class ServerControllerDriver {


    private final DriverServiceQuerryImpl driverServiceQuerry;
    private final DriverCommandServiceImpl driverCommandService;

    public ServerControllerDriver(DriverServiceQuerryImpl driverServiceQuerry, DriverCommandServiceImpl driverCommandService) {
        this.driverServiceQuerry = driverServiceQuerry;
        this.driverCommandService = driverCommandService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return new ResponseEntity<>(driverServiceQuerry.getAllDrivers().get(), HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<String> createDriver(@RequestBody DriverCreateRequest driverCreateRequest) {
        driverCommandService.create(driverCreateRequest);
        return new ResponseEntity<>("Driver created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<DriverUpdateRequest> updateDriver(@RequestBody DriverUpdateRequest driverUpdateRequest) {
        driverCommandService.update(driverUpdateRequest);
        return new ResponseEntity<>(driverUpdateRequest, HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDriver(@RequestParam("email") String email, @RequestParam("licenseNumber") String licenseNumber) {
        driverCommandService.removeByLicenseNumber(email, licenseNumber);
        return new ResponseEntity<>("Driver with license number: " + licenseNumber + " deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/allByCompany")
    public ResponseEntity<Optional<List<DriverDTO>>> getDriversByCompany(@RequestParam String registrationNumber){
        return new ResponseEntity<>(driverServiceQuerry.getAllDriversByCompany(registrationNumber),HttpStatus.OK);
    }


}
