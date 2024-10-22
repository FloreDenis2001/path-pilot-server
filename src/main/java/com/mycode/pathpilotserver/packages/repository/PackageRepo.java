package com.mycode.pathpilotserver.packages.repository;

import com.mycode.pathpilotserver.packages.models.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepo extends JpaRepository<Package, Long> {

    @Query("SELECT o FROM Package o WHERE o.customer.id = :customerId AND o.shipment.id = :shipmentId")
    Optional<Package> findByCustomerAndAndShipment(Long customerId, Long shipmentId);


    @Query("SELECT o FROM Package o WHERE o.customer.id = :customerId")
    Optional<List<Package>> getAllPackagesByCustomer(Long customerId);

    @Query("SELECT o FROM Package o WHERE o.awb = :awb")
    Optional<Package> getPackageByAwb(String awb);

    @Query("SELECT o FROM Package o WHERE o.customer.company.registrationNumber = :registerCompany AND o.status = 'UNASSIGNED'")
    Optional<List<Package>> getAllUnassignedPackages(String registerCompany);


    @Query("SELECT o FROM Package o WHERE o.customer.company.registrationNumber = :registerCompany AND o.shipment.originAddress.cityDetails.city = :city AND o.status = 'UNASSIGNED'")
    Optional<List<Package>> getAllUnassignedPackagesByCityAndCompany(String registerCompany,String city);

    @Query("SELECT o FROM Package o WHERE o.customer.company.registrationNumber = :registerCompany")
    Optional<List<Package>> getAllByRegisterCompany(String registerCompany);

}
