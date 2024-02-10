package com.mycode.pathpilotserver.driver.services;


import com.mycode.pathpilotserver.driver.models.Driver;

import java.util.Optional;

public interface DriverServiceQuerry {

    Optional<Driver> findByName(String name);

}
