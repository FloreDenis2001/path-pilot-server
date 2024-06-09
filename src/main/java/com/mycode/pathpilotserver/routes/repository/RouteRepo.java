package com.mycode.pathpilotserver.routes.repository;


import com.mycode.pathpilotserver.routes.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepo extends JpaRepository<Route, Long> {



    @Query("SELECT r FROM Route r WHERE r.company.registrationNumber = ?1")
     Optional<List<Route>> findAllByCompanyRegistrationNumber(String companyRegistrationNumber);


}

