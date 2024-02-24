package com.mycode.pathpilotserver.company.services;

import com.mycode.pathpilotserver.company.dto.CompanyCreateRequest;
import com.mycode.pathpilotserver.company.dto.UpdateCompanyRequest;
import org.springframework.stereotype.Service;

@Service
public interface CompanyCommandService {

   void createCompany(CompanyCreateRequest companyCreateRequest);
   void updateCompany(UpdateCompanyRequest updateCompanyRequest);

   void deleteCompany(String registrationNumber);


}
