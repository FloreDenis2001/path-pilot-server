package com.mycode.pathpilotserver.company.web;


import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;
import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.dto.CompanyDataDashboard;
import com.mycode.pathpilotserver.company.dto.UpdateCompanyRequest;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.services.CompanyCommandServiceImpl;
import com.mycode.pathpilotserver.company.services.CompanyQuerryServiceImpl;
import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.models.Package;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/companies")
@CrossOrigin
@Slf4j
public class ServerControllerCompany {

    private final CompanyCommandServiceImpl companyCommandService;
    private final CompanyQuerryServiceImpl companyQuerryService;

    public ServerControllerCompany(CompanyCommandServiceImpl companyCommandService, CompanyQuerryServiceImpl companyQuerryService) {
        this.companyCommandService = companyCommandService;
        this.companyQuerryService = companyQuerryService;
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByName={name}")
    public ResponseEntity<Company> findByName(@PathVariable String  name) {
        return ResponseEntity.ok(companyQuerryService.findByName(name).get());
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByEmail={email}")
    public ResponseEntity<Optional<Company>> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(companyQuerryService.findByEmail(email));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findByPhone={phone}")
    public ResponseEntity<Optional<Company>> findByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(companyQuerryService.findByPhone(phone));
    }


    @GetMapping("/findByRegistrationNumber/{registrationNumber}")
    public ResponseEntity<Optional<CompanyDTO>> findByRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.ok(companyQuerryService.findByRegistrationNumber(registrationNumber));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findAllByIndustry={industry}")
    public ResponseEntity<Optional<List<Company>>> findCompaniesByIndustry(@PathVariable String industry) {
        return ResponseEntity.ok(companyQuerryService.findCompaniesByIndustry(industry));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/create/{userEmail}")
    public ResponseEntity<String> createCompany(@RequestBody CompanyCreateRequest company ,@PathVariable String userEmail) {
        companyCommandService.createCompany(company, userEmail);
        return ResponseEntity.ok("Company created successfully");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update")
    public ResponseEntity<String> updateCompany(@RequestBody UpdateCompanyRequest company) {
        companyCommandService.updateCompany(company);
        return ResponseEntity.ok("Company updated successfully");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete={registrationNumber}")
    public ResponseEntity<String> deleteCompany(@PathVariable String registrationNumber) {
        companyCommandService.deleteCompany(registrationNumber);
        return ResponseEntity.ok("Company deleted successfully");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dashboard/company={registrationNumber}")
    public ResponseEntity<CompanyDataDashboard> getCompany(@PathVariable String registrationNumber) {
        double totalSumLastMonthPackages = companyQuerryService.getTotalSumLastMonthPackages(registrationNumber);
        int totalNumberOfPackagesLastMonth = companyQuerryService.getTotalNumberOfPackagesLastMonth(registrationNumber);
        double totalSumLastMonthOfSalary= companyQuerryService.getTotalSumLastMonthOfSalary(registrationNumber);
        double totalSumLastMonthProfit= companyQuerryService.getTotalSumLastMonthProfit(registrationNumber);
        Optional<List<DriverDTO>> bestFiveDriversByRanking = companyQuerryService.getBestFiveDriversByRanking(registrationNumber);
        Optional<List<PackageDTO>> lastFivePackagesAdded = companyQuerryService.lastFivePackagesAdded(registrationNumber);
        CompanyDataDashboard companyDataDashboard = CompanyDataDashboard.builder().
                totalSumLastMonthPackages(totalSumLastMonthPackages).
                totalNumberOfPackagesLastMonth(totalNumberOfPackagesLastMonth).
                totalSumLastMonthOfSalary(totalSumLastMonthOfSalary).
                totalSumLastMonthProfit(totalSumLastMonthProfit).
                bestFiveDriversByRanking(bestFiveDriversByRanking.get()).
                lastFivePackagesAdded(lastFivePackagesAdded.get()).build();

        return ResponseEntity.ok(companyDataDashboard);
    }


}
