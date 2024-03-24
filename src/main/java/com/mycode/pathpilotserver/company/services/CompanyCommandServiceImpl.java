package com.mycode.pathpilotserver.company.services;


import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;
import com.mycode.pathpilotserver.company.dto.UpdateCompanyRequest;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CompanyCommandServiceImpl implements CompanyCommandService {

    private final CompanyRepo companyRepo;
    private final UserRepo userRepo;

    public CompanyCommandServiceImpl(CompanyRepo companyRepo, UserRepo userRepo) {

        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
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

        User user = userRepo.findByEmail(companyCreateRequest.userEmail()).get();

        Company company = Company.builder().address(companyCreateRequest.address()).capital(companyCreateRequest.capital()).email(companyCreateRequest.email()).name(companyCreateRequest.name()).phone(companyCreateRequest.phone()).registrationNumber(companyCreateRequest.registrationNumber()).industry(companyCreateRequest.industry()).address(companyCreateRequest.address()).website(companyCreateRequest.website()).build();
        company.setLastModifiedBy(user.getUsername());
        company.setCreatedBy(user.getUsername());
        company.setCreatedDate(LocalDateTime.now());
        company.setLastModifiedDate(LocalDateTime.now());

        return company;

    }

    private Company updatedCompany(UpdateCompanyRequest updateCompanyRequest) {
        User user = userRepo.findByEmail(updateCompanyRequest.userEmail()).get();
        Company company = companyRepo.findByRegistrationNumber(updateCompanyRequest.registrationNumber()).get();
        if(updateCompanyRequest.updatedCompany().getAddress() != null){
            company.setAddress(updateCompanyRequest.updatedCompany().getAddress());
        }

        if(updateCompanyRequest.updatedCompany().getCapital() != 0){
            company.setCapital(updateCompanyRequest.updatedCompany().getCapital());
        }

        if(updateCompanyRequest.updatedCompany().getEmail() != null){
            company.setEmail(updateCompanyRequest.updatedCompany().getEmail());
        }

        if(updateCompanyRequest.updatedCompany().getIndustry() != null){
            company.setIndustry(updateCompanyRequest.updatedCompany().getIndustry());
        }

        if(updateCompanyRequest.updatedCompany().getName() != null){
            company.setName(updateCompanyRequest.updatedCompany().getName());
        }

        if(updateCompanyRequest.updatedCompany().getPhone() != null){
            company.setPhone(updateCompanyRequest.updatedCompany().getPhone());
        }

        if(updateCompanyRequest.updatedCompany().getWebsite() != null){
            company.setWebsite(updateCompanyRequest.updatedCompany().getWebsite());
        }

        company.setLastModifiedBy(user.getUsername());
        company.setLastModifiedDate(LocalDateTime.now());


        return company;
    }
}
