package com.mycode.pathpilotserver.driver.services;

import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import com.mycode.pathpilotserver.driver.dto.DriverCreateRequest;

public interface DriverCommandService {

    void create(DriverCreateRequest driverCreateRequest);
    void delete(RemoveValidationRequest removeValidationRequest);
}
