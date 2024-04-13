package com.mycode.pathpilotserver.packages.services;

import com.google.maps.errors.ApiException;
import com.mycode.pathpilotserver.orders.dto.OrderDTO;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;

import java.io.IOException;

public interface PackageCommandService {

    void createPackage(PackageDTO packageDTO) throws IOException, InterruptedException, ApiException;
}
