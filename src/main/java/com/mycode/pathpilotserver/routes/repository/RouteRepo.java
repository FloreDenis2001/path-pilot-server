package com.mycode.pathpilotserver.routes.repository;


import com.mycode.pathpilotserver.routes.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepo extends JpaRepository<Route, Long> {






}

