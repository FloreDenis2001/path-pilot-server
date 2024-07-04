package com.mycode.pathpilotserver.company.dto;

import com.mycode.pathpilotserver.company.models.Company;

public record UpdateCompanyRequest(String userEmail, String registrationNumber, Company updatedCompany) {
}
