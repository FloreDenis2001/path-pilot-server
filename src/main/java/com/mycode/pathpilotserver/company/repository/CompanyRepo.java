package com.mycode.pathpilotserver.company.repository;

import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long>{

    Optional<Company> findByName(String name);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByPhone(String phone);

    Optional<Company> findByRegistrationNumber(String registrationNumber);

    Optional<List<Company>> findCompaniesByIndustry(String industry);

}
