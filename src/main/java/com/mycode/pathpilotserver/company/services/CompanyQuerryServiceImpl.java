package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.exceptions.CompanyNotFoundException;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.exceptions.PackageNotFoundException;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.packages.repository.PackageRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyQuerryServiceImpl implements CompanyQuerryService {

    private final CompanyRepo companyRepo;
    private final PackageRepo packageRepo;
    private final DriverRepo driverRepo;

    public CompanyQuerryServiceImpl(CompanyRepo companyRepo, PackageRepo packageRepo, DriverRepo driverRepo) {
        this.companyRepo = companyRepo;
        this.packageRepo = packageRepo;
        this.driverRepo = driverRepo;
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

    @Override
    public Optional<CompanyDTO> findByRegistrationNumber(String registrationNumber) {
        return companyRepo.findByRegistrationNumber(registrationNumber)
                .map(Convertor::convertCompanyToCompanyDTO)
                .or(() -> {
                    throw new CompanyNotFoundException("Company with registration number: " + registrationNumber + " not found");
                });
    }

    @Override
    public Optional<List<Company>> findCompaniesByIndustry(String industry) {
        return companyRepo.findCompaniesByIndustry(industry)
                .or(() -> {
                    throw new CompanyNotFoundException("Companies in industry: " + industry + " not found");
                });
    }

    @Override
    public double getTotalSumLastMonthPackages(String registerCompany) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        List<Package> packages = packageRepo.getAllByRegisterCompany(registerCompany)
                .orElseThrow(() -> new PackageNotFoundException("Packages for company with registration number: " + registerCompany + " not found"));

        return packages.stream()
                .filter(pack -> pack.getOrderDate().isAfter(lastMonth.atStartOfDay()))
                .mapToDouble(Package::getTotalAmount)
                .sum();
    }

    @Override
    public int getTotalNumberOfPackagesLastMonth(String registerCompany) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        List<Package> packages = packageRepo.getAllByRegisterCompany(registerCompany)
                .orElseThrow(() -> new PackageNotFoundException("Packages for company with registration number: " + registerCompany + " not found"));

        return (int) packages.stream()
                .filter(pack -> pack.getOrderDate().isAfter(lastMonth.atStartOfDay()))
                .count();
    }

    @Override
    public double getTotalSumLastMonthOfSalary(String registerCompany) {
        List<Driver> drivers = driverRepo.findAllByCompanyRegistrationNumber(registerCompany)
                .orElseThrow(() -> new CompanyNotFoundException("Drivers for company with registration number: " + registerCompany + " not found"));

        return drivers.stream()
                .mapToDouble(Driver::getSalary)
                .sum();
    }

    @Override
    public double getTotalSumLastMonthProfit(String registerCompany) {
        double totalRevenue = getTotalSumLastMonthPackages(registerCompany);
        double totalExpenses = getTotalSumLastMonthOfSalary(registerCompany);
        return totalRevenue - totalExpenses;
    }

    @Override
    public Optional<List<DriverDTO>> getBestFiveDriversByRanking(String registerCompany) {
        List<Driver> drivers = driverRepo.bestDriversByHighestRanking(registerCompany)
                .orElseThrow(() -> new CompanyNotFoundException("Drivers for company with registration number: " + registerCompany + " not found"));

        List<DriverDTO> bestDrivers = drivers.stream()
                .sorted((d1, d2) -> Double.compare(d2.getRating(), d1.getRating()))
                .limit(5)
                .map(DriverDTO::fromDriver)
                .collect(Collectors.toList());

        return Optional.of(bestDrivers);
    }

    @Override
    public Optional<List<PackageDTO>> lastFivePackagesAdded(String registerCompany) {
        List<Package> packages = packageRepo.getAllByRegisterCompany(registerCompany)
                .orElseThrow(() -> new PackageNotFoundException("Packages for company with registration number: " + registerCompany + " not found"));

        List<Package> sortedPackages = packages.stream()
                .sorted((p1, p2) -> p2.getOrderDate().compareTo(p1.getOrderDate()))
                .limit(5)
                .collect(Collectors.toList());

        return Optional.of(Convertor.convertToPackageDTO(sortedPackages));
    }
}
