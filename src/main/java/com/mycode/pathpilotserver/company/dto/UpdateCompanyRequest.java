package com.mycode.pathpilotserver.company.dto;

import lombok.Builder;
@Builder
public record UpdateCompanyRequest(String userEmail, CompanyDTO updatedCompany) {
}
