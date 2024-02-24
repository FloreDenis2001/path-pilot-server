package com.mycode.pathpilotserver.company.services;


import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;
import com.mycode.pathpilotserver.company.dto.UpdateCompanyRequest;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.mycode.pathpilotserver.company.models.Company;

@Service
@Transactional
public class CompanyCommandServiceImpl implements CompanyCommandService {

    private final CompanyRepo companyRepo;

    public CompanyCommandServiceImpl(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    @Override
    public void createCompany(CompanyCreateRequest companyCreateRequest) {
        Optional<Company> company = companyRepo.findByName(companyCreateRequest.name());
        if (company.isPresent()) {
            throw new CompanyNotFoundException("Company with name: " + companyCreateRequest.name() + " already exists");
        } else {
            Company newCompany = buildCompany(companyCreateRequest);
            companyRepo.save(newCompany);
        }
    }

    @Override
    public void updateCompany(UpdateCompanyRequest updateCompanyRequest) {
        Optional<Company> company = companyRepo.findByRegistrationNumber(updateCompanyRequest.registrationNumber());
        if (company.isPresent()) {
            Company updatedCompany = updatedCompany(updateCompanyRequest);
            companyRepo.save(updatedCompany);
        } else {
            throw new CompanyNotFoundException("Company with registration number: " + updateCompanyRequest.registrationNumber() + " not found");
        }
    }

    @Override
    public void deleteCompany(String registrationNumber) {
        Optional<Company> company = companyRepo.findByRegistrationNumber(registrationNumber);
        if (company.isPresent()) {
            companyRepo.delete(company.get());
        } else {
            throw new CompanyNotFoundException("Company with registration number: " + registrationNumber + " not found");
        }

    }

    private Company buildCompany(CompanyCreateRequest companyCreateRequest) {
        return Company.builder().companyAddress(companyCreateRequest.address()).capital(companyCreateRequest.capital()).email(companyCreateRequest.email()).name(companyCreateRequest.name()).phone(companyCreateRequest.phone()).registrationNumber(companyCreateRequest.registrationNumber()).build();
    }

    private Company updatedCompany(UpdateCompanyRequest updateCompanyRequest) {
        return Company.builder().companyAddress(updateCompanyRequest.updatedCompany().getCompanyAddress()).registrationNumber(updateCompanyRequest.registrationNumber()).capital(updateCompanyRequest.updatedCompany().getCapital()).email(updateCompanyRequest.updatedCompany().getEmail()).name(updateCompanyRequest.updatedCompany().getName()).phone(updateCompanyRequest.updatedCompany().getPhone()).build();
    }
}
