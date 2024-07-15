package com.mycode.pathpilotserver.company.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class CompanyTest {

    @Autowired
    private CompanyRepo companyRepo;

    private Company company;

    @AfterEach
    void tearDown() {
        companyRepo.deleteAll();
    }

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .name("Test Company")
                .registrationNumber("123456789")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.company.com")
                .build();
        companyRepo.save(company);
    }

    @Test
    void testFindByName() {
        Optional<Company> foundCompany = companyRepo.findByName("Test Company");
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getName()).isEqualTo("Test Company");
    }

    @Test
    void testFindByNameNotFound() {
        Optional<Company> foundCompany = companyRepo.findByName("Nonexistent Company");
        assertThat(foundCompany).isNotPresent();
    }

    @Test
    void testFindByEmail() {
        Optional<Company> foundCompany = companyRepo.findByEmail("test@company.com");
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getEmail()).isEqualTo("test@company.com");
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<Company> foundCompany = companyRepo.findByEmail("nonexistent@company.com");
        assertThat(foundCompany).isNotPresent();
    }

    @Test
    void testFindByPhone() {
        Optional<Company> foundCompany = companyRepo.findByPhone("123-456-7890");
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getPhone()).isEqualTo("123-456-7890");
    }

    @Test
    void testFindByPhoneNotFound() {
        Optional<Company> foundCompany = companyRepo.findByPhone("000-000-0000");
        assertThat(foundCompany).isNotPresent();
    }

    @Test
    void testFindByRegistrationNumber() {
        Optional<Company> foundCompany = companyRepo.findByRegistrationNumber("123456789");
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getRegistrationNumber()).isEqualTo("123456789");
    }

    @Test
    void testFindByRegistrationNumberNotFound() {
        Optional<Company> foundCompany = companyRepo.findByRegistrationNumber("000000000");
        assertThat(foundCompany).isNotPresent();
    }

    @Test
    void testFindCompaniesByIndustry() {
        Optional<List<Company>> companies = companyRepo.findCompaniesByIndustry("Tech");
        assertThat(companies).isPresent();
        assertThat(companies.get()).hasSize(1);
        assertThat(companies.get().get(0).getIndustry()).isEqualTo("Tech");
    }

    @Test
    void testFindCompaniesByIndustryNotFound() {
        Optional<List<Company>> companies = companyRepo.findCompaniesByIndustry("Nonexistent Industry");
        assertThat(companies).isPresent();
        assertThat(companies.get()).isEmpty();
    }

    @Test
    void testSaveCompany() {
        Company newCompany = Company.builder()
                .name("New Company")
                .registrationNumber("987654321")
                .industry("Finance")
                .income(2000000)
                .phone("987-654-3210")
                .email("new@company.com")
                .website("www.newcompany.com")
                .build();
        Company savedCompany = companyRepo.save(newCompany);
        assertThat(savedCompany).isNotNull();
        assertThat(savedCompany.getId()).isNotNull();
        assertThat(savedCompany.getName()).isEqualTo("New Company");
    }

    @Test
    void testDeleteCompany() {
        companyRepo.delete(company);
        Optional<Company> deletedCompany = companyRepo.findById(company.getId());
        assertThat(deletedCompany).isNotPresent();
    }

    @Test
    void testFindAllCompanies() {
        List<Company> companies = companyRepo.findAll();
        assertThat(companies).isNotEmpty();
        assertThat(companies).hasSize(1);
    }
}
