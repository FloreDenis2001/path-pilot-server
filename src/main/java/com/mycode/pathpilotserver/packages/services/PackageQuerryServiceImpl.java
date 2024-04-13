package com.mycode.pathpilotserver.packages.services;

import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageQuerryServiceImpl implements PackageQuerryService {

    private final UserRepo userRepo;

    private final PackageRepo packageRepo;

    public PackageQuerryServiceImpl(UserRepo userRepo, PackageRepo packageRepo) {
        this.userRepo = userRepo;
        this.packageRepo = packageRepo;
    }

    @Override
    public Optional<List<PackageRequest>> getAllPackagesByCustomer(Long customerId) {
        Optional<List<Package>> packages = packageRepo.getAllPackagesByCustomer(customerId);
        if (packages.isEmpty()) {
            throw new UserNotFoundException("Packages for user with id: " + customerId + " not found");
        }

        List<PackageRequest> packagesRequest = Convertor.convertToPackageRequest(packages.get());

        return Optional.of(packagesRequest);
    }
}