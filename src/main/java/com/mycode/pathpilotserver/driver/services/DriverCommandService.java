package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;
import com.mycode.pathpilotserver.driver.dto.DriverUpdateRequest;

public interface DriverCommandService {

    void create(DriverCreateRequest driverCreateRequest);

    void update(DriverUpdateRequest driverUpdateRequest);

    void removeByLicenseNumber(String email, String licenseNumber);


}
