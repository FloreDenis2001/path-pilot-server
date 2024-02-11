package com.mycode.pathpilotserver.company.services;


import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyCommandServiceImpl implements CompanyCommandService{

    private final CompanyRepo companyRepo;

    public CompanyCommandServiceImpl(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }
    @Override
    public void createCompany() {



    }

    @Override
    public void updateCompany() {

    }

    @Override
    public void deleteCompany() {

    }
}
