package com.mycode.pathpilotserver.driver.services;


import com.mycode.pathpilotserver.driver.models.Driver;

import java.util.Optional;

public interface DriverQuerryService {


    Optional<Driver> findByLicenseNumber(String licenseNumber);

}
