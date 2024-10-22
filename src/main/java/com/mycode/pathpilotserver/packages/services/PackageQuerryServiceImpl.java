package com.mycode.pathpilotserver.packages.services;

import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageQuerryServiceImpl implements PackageQuerryService {


    private final PackageRepo packageRepo;

    public PackageQuerryServiceImpl(PackageRepo packageRepo) {
        this.packageRepo = packageRepo;
    }

    @Override
    public Optional<List<PackageDTO>> getAllPackagesByCustomer(Long customerId) {
        Optional<List<Package>> packages = packageRepo.getAllPackagesByCustomer(customerId);
        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Packages for user with id: " + customerId + " not found");
        }
        List<PackageDTO> packageDTOS = Convertor.convertToPackageDTO(packages.get());
        return Optional.of(packageDTOS);
    }

    @Override
    public Optional<List<Package>> getAllUnassignedPackages(String registerCompany) {
        Optional<List<Package>> packages = packageRepo.getAllUnassignedPackages(registerCompany);
        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Packages for company with registration number: " + registerCompany + " not found");
        }
        return packages;
    }



}