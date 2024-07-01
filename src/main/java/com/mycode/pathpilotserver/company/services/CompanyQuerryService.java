package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.models.Package;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CompanyQuerryService {


    Optional<Company> findByName(String name);


    Optional<Company> findByEmail(String email);


    Optional<Company> findByPhone(String phone);

    Optional<CompanyDTO> findByRegistrationNumber(String registrationNumber);


    Optional<List<Company>> findCompaniesByIndustry(String industry);

    double getTotalSumLastMonthPackages(String registerCompany);

    int getTotalNumberOfPackagesLastMonth(String registerCompany);

    double getTotalSumLastMonthOfSalary(String registerCompany);

    double getTotalSumLastMonthProfit(String registerCompany);

    Optional<List<DriverDTO>>getBestFiveDriversByRanking(String registerCompany);

    Optional<List<PackageDTO>> lastFivePackagesAdded(String registerCompany);


}
