package com.mycode.pathpilotserver.driver.web;

import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.services.DriverCommandServiceImpl;
import com.mycode.pathpilotserver.driver.services.DriverServiceQuerryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByName={name}")
    public ResponseEntity<Driver> findByName(@PathVariable String name) {
        return ResponseEntity.ok(driverServiceQuerry.findByName(name).get());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByLicenseNumber={licenseNumber}")
    public ResponseEntity<Driver> findByLicenseNumber(@PathVariable String licenseNumber) {
        return ResponseEntity.ok(driverServiceQuerry.findByLicenseNumber(licenseNumber).get());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<Driver> create(@RequestBody DriverCreateRequest driverCreateRequest) {
        driverCommandService.create(driverCreateRequest);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete")
    public ResponseEntity<Driver> delete(@RequestBody RemoveValidationRequest removeValidationRequest) {
        driverCommandService.delete(removeValidationRequest);
        return ResponseEntity.ok().build();
    }



}
