package com.mycode.pathpilotserver.packages.services;

import com.google.maps.errors.ApiException;
import com.mycode.pathpilotserver.orders.dto.OrderDTO;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageRequest;

import java.io.IOException;

public interface PackageCommandService {

    void createPackage(PackageRequest packageRequest) throws IOException, InterruptedException, ApiException;
    void deletePackage(String awb);

    void editPackage(String awb, PackageRequest packageRequest) throws IOException, InterruptedException, ApiException;
}
