package com.mycode.pathpilotserver.packages.services;

import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageRequest;
import com.mycode.pathpilotserver.packages.models.Package;

import java.util.List;
import java.util.Optional;

public interface PackageQuerryService {

    Optional<List<PackageDTO>> getAllPackagesByCustomer(Long customerId);;
}
