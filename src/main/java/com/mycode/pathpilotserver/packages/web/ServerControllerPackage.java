package com.mycode.pathpilotserver.packages.web;

import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.services.PackageCommandServiceImpl;
import com.mycode.pathpilotserver.packages.services.PackageQuerryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PackageRequest> create(@RequestBody PackageRequest packageRequest) {
        packageServiceCommand.createPackage(packageRequest);
        return ResponseEntity.ok(packageRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findAllByCustomer={customerId}")
    public ResponseEntity<List<PackageDTO>> findAllByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(packageServiceQuerry.getAllPackagesByCustomer(customerId).get());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findAllUnassignedPackages={registerCompany}")
    public ResponseEntity<List<Package>> findAllUnassignedPackages(@PathVariable String registerCompany) {
        return ResponseEntity.ok(packageServiceQuerry.getAllUnassignedPackages(registerCompany).get());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{awb}")
    public ResponseEntity<String> delete(@PathVariable String awb) {
        packageServiceCommand.deletePackage(awb);
        return ResponseEntity.ok("Package with awb: " + awb + " deleted");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/edit/{awb}")
    public ResponseEntity<PackageRequest> edit(@PathVariable String awb, @RequestBody PackageRequest packageRequest) {
        packageServiceCommand.editPackage(awb, packageRequest);
        return ResponseEntity.ok(packageRequest);
    }
}
