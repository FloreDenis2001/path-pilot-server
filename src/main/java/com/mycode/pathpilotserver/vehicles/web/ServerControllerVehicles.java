package com.mycode.pathpilotserver.vehicles.web;


import com.mycode.pathpilotserver.vehicles.dto.CreateVehicleRequest;
import com.mycode.pathpilotserver.vehicles.dto.UpdatedVehicleRequest;
import com.mycode.pathpilotserver.vehicles.models.FuelType;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import com.mycode.pathpilotserver.vehicles.services.VehicleServiceCommandImpl;
import com.mycode.pathpilotserver.vehicles.services.VehicleServiceQuerryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin
@Slf4j
public class ServerControllerVehicles {

    private final VehicleServiceQuerryImpl vehicleServiceQuerry;
    private final VehicleServiceCommandImpl vehicleCommandService;

    public ServerControllerVehicles(VehicleServiceQuerryImpl vehicleServiceQuerry, VehicleServiceCommandImpl vehicleCommandService) {
        this.vehicleServiceQuerry = vehicleServiceQuerry;
        this.vehicleCommandService = vehicleCommandService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByRegistrationNumber={registrationNumber}")
    public ResponseEntity<Vehicle> findByRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.ok(vehicleServiceQuerry.findByRegistrationNumber(registrationNumber).get());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByType={type}")
    public ResponseEntity<List<Vehicle>> findByType(@PathVariable FuelType type) {
        return ResponseEntity.ok(vehicleServiceQuerry.findByFuelType(type).get());
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findAll")
    public ResponseEntity<List<Vehicle>> findAll() {
        return ResponseEntity.ok(vehicleServiceQuerry.findAll().get());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByCapacity={capacity}")
    public ResponseEntity<List<Vehicle>> findByCapacity(@PathVariable int capacity) {
        return ResponseEntity.ok(vehicleServiceQuerry.findByCapacity(capacity).get());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<CreateVehicleRequest> create(@RequestBody CreateVehicleRequest vehicle) {
        vehicleCommandService.create(vehicle);
        return ResponseEntity.ok(vehicle);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete={registrationNumber}")
    public ResponseEntity<String> delete(@PathVariable String registrationNumber) {
        vehicleCommandService.delete(registrationNumber);
        return ResponseEntity.ok("Vehicle with registration number " + registrationNumber + " was deleted");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update")
    public ResponseEntity<UpdatedVehicleRequest> update(@RequestBody UpdatedVehicleRequest vehicle) {
        vehicleCommandService.update(vehicle);
        return ResponseEntity.ok(vehicle);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getVehiclesByCompanyRegistrationNumber={registrationNumber}")
    public ResponseEntity<List<Vehicle>> getVehiclesByCompanyRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.ok(vehicleServiceQuerry.getVehiclesByCompanyRegistrationNumber(registrationNumber).get());
    }
}
