package com.mycode.pathpilotserver.packages.services;

import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.models.Package;

import java.util.List;
import java.util.Optional;

public interface PackageQuerryService {

    Optional<List<PackageRequest>> getAllPackagesByCustomer(Long customerId);;
}
