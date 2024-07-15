package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;
import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.dto.UpdateCompanyRequest;
import com.mycode.pathpilotserver.company.exceptions.CompanyAlreadyExistException;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.exceptions.CompanyValidationException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyCommandServiceImplTest {

    @Mock
    private CompanyRepo companyRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private DriverRepo driverRepo;

    @InjectMocks
    private CompanyCommandServiceImpl companyCommandService;



    @Test
    void testCreateCompany_Success() {
        CompanyCreateRequest request = new CompanyCreateRequest(
                "Test Company", "Tech", 1000000, "REG123",
                "www.testcompany.com", new Address(), "123-456-7890", "test@company.com"
        );

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("test@company.com")).thenReturn(Optional.of(new Customer()));

        companyCommandService.createCompany(request, "test@company.com");

        verify(companyRepo, times(1)).save(any(Company.class));
    }

    @Test
    void testCreateCompany_CompanyAlreadyExists() {
        CompanyCreateRequest request = new CompanyCreateRequest(
                "Test Company", "Tech", 1000000, "REG123",
                "www.testcompany.com", new Address(), "123-456-7890", "test@company.com"
        );

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.of(new Company()));

        CompanyAlreadyExistException thrown = assertThrows(CompanyAlreadyExistException.class, () ->
                companyCommandService.createCompany(request, "test@company.com")
        );

        assertEquals("Company with registration number : REG123 already exists", thrown.getMessage());
    }
    @Test
    void testCreateCompany_ValidationException_ForInvalidRequests() {
        CompanyCreateRequest request = new CompanyCreateRequest(
                "",
                "",
                -100,
                "",
                "",
                null,
                "",
                ""
        );

        assertThrows(CompanyValidationException.class, () ->
                companyCommandService.createCompany(request, "test@company.com")
        );
    }

    @Test
    void testUpdateCompany_ValidationException_ForInvalidRequests() {
        UpdateCompanyRequest request = new UpdateCompanyRequest(
                "",
                new CompanyDTO("REG123", "Updated Company", "Tech", "www.updatedcompany.com", "098-765-4321", "updated@company.com", -500, new AddressDTO("Bucuresti", "15", "Damovicioarei", "Romania", "447065"))
        );

        Company existingCompany = Company.builder()
                .registrationNumber("REG123")
                .name("Old Company")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.testcompany.com")
                .build();

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.of(existingCompany));

        assertThrows(UserNotFoundException.class, () ->
                companyCommandService.updateCompany(request)
        );
    }




    @Test
    void testCreateCompany_UserNotFound() {
        CompanyCreateRequest request = new CompanyCreateRequest(
                "Test Company", "Tech", 1000000, "REG123",
                "www.testcompany.com", new Address(), "123-456-7890", "test@company.com"
        );

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("test@company.com")).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () ->
                companyCommandService.createCompany(request, "test@company.com")
        );

        assertEquals("User with email test@company.com not found", thrown.getMessage());
    }

    @Test
    void testUpdateCompany_Success() {
        UpdateCompanyRequest request = new UpdateCompanyRequest(
                "test@company.com",
                new CompanyDTO("REG123", "Updated Company", "Tech", "www.updatedcompany.com", "098-765-4321", "updated@company.com", 2000000, new AddressDTO("Bucuresti", "15", "Damovicioarei", "Romania", "447065"))
        );

        Company existingCompany = Company.builder()
                .registrationNumber("REG123")
                .name("Old Company")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.testcompany.com")
                .build();

        User user = Customer.builder()
                .username("customer_user")
                .password("password")
                .email("customer@example.com")
                .role(UserRole.CUSTOMER)
                .firstName("Customer")
                .lastName("User")
                .phone("1234567890")
                .address(null)
                .company(existingCompany)
                .subscriptionType(SubscriptionType.BASIC)
                .build();

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.of(existingCompany));
        when(userRepo.findByEmail("test@company.com")).thenReturn(Optional.of(user));

        companyCommandService.updateCompany(request);

        verify(companyRepo, times(1)).save(existingCompany);
        assertEquals("Updated Company", existingCompany.getName());
        assertEquals("098-765-4321", existingCompany.getPhone());
        assertEquals("updated@company.com", existingCompany.getEmail());
        assertEquals("www.updatedcompany.com", existingCompany.getWebsite());
        assertEquals("customer_user", existingCompany.getLastModifiedBy());
    }

    @Test
    void testUpdateCompany_CompanyNotFound() {
        UpdateCompanyRequest request = new UpdateCompanyRequest(
                "test@company.com",
                new CompanyDTO("REG123", "Updated Company", "Tech", "www.updatedcompany.com", "098-765-4321", "updated@company.com", 2000000, new AddressDTO("Bucuresti", "15", "Damovicioarei", "Romania", "447065"))
        );

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyCommandService.updateCompany(request)
        );

        assertEquals("Company with registration number: REG123 not found", thrown.getMessage());
    }



    @Test
    void testUpdateCompany_UserNotFound() {
        UpdateCompanyRequest request = new UpdateCompanyRequest(
                "test@company.com",
                new CompanyDTO("REG123", "Updated Company", "Tech", "www.updatedcompany.com", "098-765-4321", "updated@company.com", 2000000, new AddressDTO("Bucuresti", "15", "Damovicioarei", "Romania", "447065"))
        );

        Company existingCompany = Company.builder()
                .registrationNumber("REG123")
                .name("Old Company")
                .industry("Tech")
                .income(1000000)
                .phone("123-456-7890")
                .email("test@company.com")
                .website("www.testcompany.com")
                .build();

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.of(existingCompany));
        when(userRepo.findByEmail("test@company.com")).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () ->
                companyCommandService.updateCompany(request)
        );

        assertEquals("User with email test@company.com not found", thrown.getMessage());
    }

    @Test
    void testDeleteCompany_Success() {
        Company existingCompany = Company.builder()
                .registrationNumber("REG123")
                .name("Test Company")
                .build();

        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.of(existingCompany));

        companyCommandService.deleteCompany("REG123");

        verify(companyRepo, times(1)).delete(existingCompany);
    }

    @Test
    void testDeleteCompany_CompanyNotFound() {
        when(companyRepo.findByRegistrationNumber("REG123")).thenReturn(Optional.empty());

        CompanyNotFoundException thrown = assertThrows(CompanyNotFoundException.class, () ->
                companyCommandService.deleteCompany("REG123")
        );

        assertEquals("Company with registration number: REG123 not found", thrown.getMessage());
    }

    @Test
    void testResetDriversSalary() {
        Driver driver1 = new Driver();
        driver1.setSalary(1000.0);
        Driver driver2 = new Driver();
        driver2.setSalary(1500.0);

        when(driverRepo.findAll()).thenReturn(List.of(driver1, driver2));

        companyCommandService.resetDriversSalary();

        verify(driverRepo, times(1)).findAll();
        assertEquals(0.0, driver1.getSalary(), "Driver 1 salary should be reset to 0");
        assertEquals(0.0, driver2.getSalary(), "Driver 2 salary should be reset to 0");
        verify(driverRepo, times(1)).flush();
    }


}
