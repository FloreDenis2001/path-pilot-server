package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;
import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.dto.UpdateCompanyRequest;
import com.mycode.pathpilotserver.company.exceptions.CompanyAlreadyExistException;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

@Service
@Transactional
public class CompanyCommandServiceImpl implements CompanyCommandService {

    private final CompanyRepo companyRepo;
    private final UserRepo userRepo;
    private final DriverRepo driverRepo;

    public CompanyCommandServiceImpl(CompanyRepo companyRepo, UserRepo userRepo, DriverRepo driverRepo) {
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
        this.driverRepo = driverRepo;
    }

    @Override
    public void createCompany(CompanyCreateRequest companyCreateRequest, String userEmail) {
        if (companyRepo.findByRegistrationNumber(companyCreateRequest.registrationNumber()).isPresent()) {
            throw new CompanyAlreadyExistException("Company with registration number : " + companyCreateRequest.name() + " already exists");
        }

        userRepo.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));
        Company newCompany = buildCompany(companyCreateRequest);
        companyRepo.save(newCompany);
    }

    @Override
    public void updateCompany(UpdateCompanyRequest updateCompanyRequest) {
        Company company = companyRepo.findByRegistrationNumber(updateCompanyRequest.updatedCompany().registrationNumber()).orElseThrow(() -> new CompanyNotFoundException("Company with registration number: " + updateCompanyRequest.updatedCompany().registrationNumber() + " not found"));

        User user = userRepo.findByEmail(updateCompanyRequest.userEmail()).orElseThrow(() -> new UserNotFoundException("User with email " + updateCompanyRequest.userEmail() + " not found"));

        applyCompanyUpdates(company, updateCompanyRequest.updatedCompany());
        company.setLastModifiedBy(user.getUsername());
        company.setLastModifiedDate(LocalDateTime.now());

        companyRepo.save(company);
    }
    @Override
    public void deleteCompany(String registrationNumber) {
        Company company = companyRepo.findByRegistrationNumber(registrationNumber).orElseThrow(() -> new CompanyNotFoundException("Company with registration number: " + registrationNumber + " not found"));

        companyRepo.delete(company);
    }


    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetDriversSalary() {
        driverRepo.findAll().forEach(driver -> driver.setSalary(0.0));
        driverRepo.flush();
    }

    private Company buildCompany(CompanyCreateRequest companyCreateRequest) {
        return Company.builder().address(companyCreateRequest.address()).income(companyCreateRequest.capital()).email(companyCreateRequest.email()).name(companyCreateRequest.name()).phone(companyCreateRequest.phone()).registrationNumber(companyCreateRequest.registrationNumber()).industry(companyCreateRequest.industry()).website(companyCreateRequest.website()).build();
    }

    private void applyCompanyUpdates(Company company, CompanyDTO updatedCompany) {
        updateIfNotNull(updatedCompany.address(), addressDTO -> company.setAddress(Convertor.convertAddressDTOToAddress(addressDTO)));
        updateIfNotNull(updatedCompany.email(), company::setEmail);
        updateIfPositive(updatedCompany.capital(), company::setIncome);
        updateIfNotNull(updatedCompany.industry(), company::setIndustry);
        updateIfNotNull(updatedCompany.name(), company::setName);
        updateIfNotNull(updatedCompany.phone(), company::setPhone);
        updateIfNotNull(updatedCompany.website(), company::setWebsite);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void updateIfPositive(double value, DoubleConsumer setter) {
        if (value > 0) {
            setter.accept(value);
        }
    }

}
