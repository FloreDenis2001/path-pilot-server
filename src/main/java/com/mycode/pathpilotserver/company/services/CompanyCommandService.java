package com.mycode.pathpilotserver.company.services;

import org.springframework.stereotype.Service;

@Service
public interface CompanyCommandService {

   void createCompany();
   void updateCompany();

   void deleteCompany();


}
