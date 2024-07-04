package com.mycode.pathpilotserver.driver.repository;

import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.driver.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepo extends JpaRepository<Driver, Long> {


    Optional<Driver> findByLicenseNumber(String licenseNumber);


    Optional<List<Driver>> findAllByCompanyRegistrationNumber(String registration);

    @Query("SELECT d FROM Driver d WHERE d.company.registrationNumber = ?1 AND d.isAvailable = true")
    Optional<List<Driver>> findAllByCompanyRegistrationNumberAndIsAvailableTrue(String registration);


    @Query("SELECT d FROM Driver d WHERE d.company.registrationNumber = ?1 ORDER BY d.rating DESC")
    Optional<List<Driver>> bestDriversByHighestRanking(String registrationNumber);

    Optional<Driver> findByLicenseNumberAndCompany(String licenseNumber, Company company);


}
