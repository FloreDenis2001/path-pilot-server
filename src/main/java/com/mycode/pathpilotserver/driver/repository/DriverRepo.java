package com.mycode.pathpilotserver.driver.repository;

import com.mycode.pathpilotserver.driver.models.Driver;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepo extends JpaRepository<Driver, Long> {

    
    Optional<Driver> findByName(String name);

    Optional<Driver> findByLicenseNumber(String licenseNumber);



}
