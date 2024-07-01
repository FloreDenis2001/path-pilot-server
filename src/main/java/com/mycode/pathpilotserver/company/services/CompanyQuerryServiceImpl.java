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

    @Override
    public double getTotalSumLastMonthPackages(String registerCompany) {

        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        List<Package> packages = packageRepo.getAllByRegisterCompany(registerCompany)
                .orElseThrow(() -> new PackageNotFoundException("Packages for company with registration number: " + registerCompany + " not found"));
        double totalSum = packages.stream()
                .filter(pack -> pack.getOrderDate().isAfter(lastMonth.atStartOfDay()))
                .mapToDouble(Package::getTotalAmount)
                .sum();
        return Double.parseDouble(String.format("%.2f", totalSum));
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

        double totalSalary = drivers.stream()
                .mapToDouble(Driver::getSalary)
                .sum();

        return Double.parseDouble(String.format("%.2f", totalSalary));
    }


    @Override
    public double getTotalSumLastMonthProfit(String registerCompany) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        List<Package> packages = packageRepo.getAllByRegisterCompany(registerCompany)
                .orElseThrow(() -> new PackageNotFoundException(
                        "Packages for company with registration number: " + registerCompany + " not found"
                ));
        double totalRevenue = packages.stream()
                .filter(pack -> pack.getOrderDate().isAfter(lastMonth.atStartOfDay()))
                .mapToDouble(Package::getTotalAmount)
                .sum();
        double totalExpenses = getTotalSumLastMonthOfSalary(registerCompany);
        double profit = totalRevenue - totalExpenses;
        return Double.parseDouble(String.format("%.2f", profit));
    }


    @Override
    public Optional<List<DriverDTO>> getBestFiveDriversByRanking(String registerCompany) {
        Optional<List<Driver>> optionalDrivers = driverRepo.bestDriversByHighestRanking(registerCompany);

        if (optionalDrivers.isEmpty()) {
            throw new CompanyNotFoundException("Drivers for company with registration number: " + registerCompany + " not found");
        }

        List<Driver> drivers = optionalDrivers.get();

        List<Driver> bestDrivers = drivers.size() > 5 ? drivers.subList(0, 5) : drivers;

        return Optional.of(DriverDTO.fromList(bestDrivers));
    }

    @Override
    public Optional<List<PackageDTO>> lastFivePackagesAdded(String registerCompany) {
        Optional<List<Package>> packages = packageRepo.getAllByRegisterCompany(registerCompany);
        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Packages for company with registration number: " + registerCompany + " not found");
        }

        List<Package> sortedPackages = packages.get().stream()
                .sorted((p1, p2) ->p2.getOrderDate().compareTo(p1.getOrderDate()))
                .limit(5)
                .collect(Collectors.toList());

        return Optional.of(Convertor.convertToPackageDTO(sortedPackages));
    }


}
