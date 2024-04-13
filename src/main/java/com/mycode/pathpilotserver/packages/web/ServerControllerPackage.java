package com.mycode.pathpilotserver.packages.web;

import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.services.PackageCommandServiceImpl;
import com.mycode.pathpilotserver.packages.services.PackageQuerryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycode.pathpilotserver.packages.models.Package;
import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
@Slf4j
@CrossOrigin
public class ServerControllerPackage {

    private final PackageCommandServiceImpl packageServiceCommand;

    private final PackageQuerryServiceImpl packageServiceQuerry;

    public ServerControllerPackage(PackageCommandServiceImpl packageServiceCommand, PackageQuerryServiceImpl packageServiceQuerry) {
        this.packageServiceCommand = packageServiceCommand;
        this.packageServiceQuerry = packageServiceQuerry;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<PackageDTO> create(@RequestBody PackageDTO packageDTO) {
        packageServiceCommand.createPackage(packageDTO);
        return ResponseEntity.ok(packageDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findAllByCustomer={customerId}")
    public ResponseEntity<List<PackageRequest>> findAllByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(packageServiceQuerry.getAllPackagesByCustomer(customerId).get());
    }
}
