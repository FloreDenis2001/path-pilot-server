package com.mycode.pathpilotserver.driver.repository;

import com.mycode.pathpilotserver.driver.models.Driver;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends JpaRepository<Driver, Long> {

    @EntityGraph(attributePaths = {"user", "shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Driver findByName(String name);

    @EntityGraph(attributePaths = {"user", "shipmentDetails"}, type = EntityGraph.EntityGraphType.LOAD)
    Driver findByUserEmail(String email);


}
