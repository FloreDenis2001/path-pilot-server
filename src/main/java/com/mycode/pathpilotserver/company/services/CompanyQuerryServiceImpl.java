package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyQuerryServiceImpl implements CompanyQuerryService {

    private final CompanyRepo companyRepo;


    public CompanyQuerryServiceImpl(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }


    @Override
    public Optional<Company> findByName(String name) {
        Optional<Company> company = companyRepo.findByName(name);
        if (company.isPresent()) {
            return company;
        } else {
            throw new CompanyNotFoundException("Company with name: " + name + " not found");
        }
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        Optional<Company> company = companyRepo.findByEmail(email);
        if (company.isPresent()) {
            return company;
        } else {
            throw new CompanyNotFoundException("Company with email: " + email + " not found");
        }
    }

    @Override
    public Optional<Company> findByPhone(String phone) {

        Optional<Company> company = companyRepo.findByPhone(phone);
        if (company.isPresent()) {
            return company;
        } else {
            throw new CompanyNotFoundException("Company with phone: " + phone + " not found");
        }
    }


  //todo : apare erroare din cauza volumului de date

    @Override
    public Optional<CompanyDTO> findByRegistrationNumber(String registrationNumber) {
        Optional<Company> company = companyRepo.findByRegistrationNumber(registrationNumber);
        if (company.isPresent()) {
            return Optional.of(Convertor.convertCompanyToCompanyDTO(company.get()));
        } else {
            throw new CompanyNotFoundException("Company with registration number: " + registrationNumber + " not found");
        }
    }



    @Override
    public Optional<List<Company>> findCompaniesByIndustry(String industry) {
        Optional<List<Company>> companies = companyRepo.findCompaniesByIndustry(industry);
        if (companies.isPresent()) {
            return companies;
        } else {
            throw new CompanyNotFoundException("Company with industry: " + industry + " not found");
        }
    }
}
