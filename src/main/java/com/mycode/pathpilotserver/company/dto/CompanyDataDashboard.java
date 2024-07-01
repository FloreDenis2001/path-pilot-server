package com.mycode.pathpilotserver.company.dto;

import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.models.Package;
import lombok.Builder;

import java.util.List;

@Builder
public record CompanyDataDashboard(double totalSumLastMonthPackages, int totalNumberOfPackagesLastMonth,
                                   double totalSumLastMonthOfSalary, double totalSumLastMonthProfit,
                                   List<DriverDTO> bestFiveDriversByRanking, List<PackageDTO > lastFivePackagesAdded) {

    @Override
    public String toString() {
        return "CompanyDataDashboard{" +
                "totalSumLastMonthPackages=" + totalSumLastMonthPackages +
                ", totalNumberOfPackagesLastMonth=" + totalNumberOfPackagesLastMonth +
                ", totalSumLastMonthOfSalary=" + totalSumLastMonthOfSalary +
                ", totalSumLastMonthProfit=" + totalSumLastMonthProfit +
                ", bestFiveDriversByRanking=" + bestFiveDriversByRanking +
                ", lastFivePackagesAdded=" + lastFivePackagesAdded +
                '}';
    }
}
