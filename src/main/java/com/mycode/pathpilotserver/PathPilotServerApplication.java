package com.mycode.pathpilotserver;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipmentDetails.repository.ShipmentDetailsRepo;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.user.dto.LoginUserRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import com.mycode.pathpilotserver.user.services.UserServiceCommandImpl;
import com.mycode.pathpilotserver.vehicles.repository.VehicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PathPilotServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PathPilotServerApplication.class, args);
    }


    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(CompanyRepo companyRepo, UserRepo userRepo
            , CustomerRepo customerRepo, DriverRepo driverRepo, VehicleRepo vehicleRepo, UserServiceCommandImpl userServiceCommandImpl
            , ShipmentRepo shipmentRepo, ShipmentDetailsRepo shipmentDetailsRepo, OrderRepo orderRepo) {
        return args -> {
            Address address = new Address();
            address.setCity("New York");
            address.setCountry("USA");
            address.setStreet("Wall Street");
            address.setStreetNumber("1234");
            address.setPostalCode("12345");


            Company company = new Company();
            company.setName("PathPilot");
            company.setIndustry("Transportation");
            company.setRegistrationNumber("1234567890");
            company.setWebsite("www.pathpilot.com");
            company.setCompanyAddress(address);
            company.setPhone("1234567890");
            company.setEmail("daskda@yahoo.com");
            companyRepo.saveAndFlush(company);


            Driver driver = new Driver();
            driver.setUsername("johnSmile");
            driver.setPassword("123");
            driver.setEmail("jhon@yahoo.com");
            driver.setRole("DRIVER");
            driver.setCompany(company);
            driver.setName("John");
            driver.setPhone("1234567890");
            driver.setLicenseNumber("1234567890");
            driverRepo.saveAndFlush(driver);


            Customer customer = new Customer();
            customer.setUsername("johnSmile");
            customer.setPassword("123");
            customer.setEmail("denis2@yahoo.com");
            customer.setRole("CUSTOMER");
            customer.setCompany(company);
            customer.setName("Denis");
            customer.setPhone("123456");
            customerRepo.saveAndFlush(customer);

//            LoginUserRequest loginUserRequest = LoginUserRequest.builder().email("jhon@yahoo.com").password("123").build();
//            userServiceCommandImpl.deleteUser(loginUserRequest);


//            UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().email("floredenis907@yahoo.com").password("parolaNoua123")
//                    .newUser(User.builder().email("floredenis907@yahoo.com").password("parolaNoua123").username("denisflore120").build()).build();
//            userServiceCommandImpl.updateUser(updateUserRequest);


        };
    }

}
