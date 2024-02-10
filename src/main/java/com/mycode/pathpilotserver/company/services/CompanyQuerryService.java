package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.company.models.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CompanyQuerryService {


    Optional<Company> findByName(String name);


    Optional<Company> findByEmail(String email);


    Optional<Company> findByPhone(String phone);


    Optional<Company> findByRegistrationNumber(String registrationNumber);

    Optional<List<Company>> findCompaniesByCapital(double capital);

    Optional<List<Company>> findCompaniesByIndustry(String industry);


}
