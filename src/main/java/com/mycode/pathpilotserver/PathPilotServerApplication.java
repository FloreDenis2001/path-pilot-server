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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.LocalDateTime;
import java.util.Arrays;

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

//            Address address = new Address();
//            address.setCity("New York");
//            address.setCountry("USA");
//            address.setStreet("Wall Street");
//            address.setStreetNumber("1234");
//            address.setPostalCode("12345");
//
//
//            Company company = new Company();
//            company.setName("PathPilot");
//            company.setIndustry("Transportation");
//            company.setRegistrationNumber("1234567890");
//            company.setWebsite("www.pathpilot.com");
//            company.setAddress(address);
//            company.setPhone("1234567890");
//            company.setEmail("floredenis907@yahoo.com");
//            company.setCreatedBy("Flore Denis");
//            company.setCapital(100000);
//            company.setCreatedDate(LocalDateTime.now());
//            company.setLastModifiedDate(LocalDateTime.now());
//            company.setLastModifiedBy("Flore Denis");
//            companyRepo.saveAndFlush(company);
//
//            Driver driver = new Driver();
//            driver.setUsername("johnSmile");
//            driver.setPassword("123");
//            driver.setEmail("jhon@yahoo.com");
//            driver.setRole("DRIVER");
//            driver.setCompany(company);
//            driver.setName("John");
//            driver.setPhone("1234567890");
//            driver.setLicenseNumber("1234567890");
//            driverRepo.saveAndFlush(driver);
//
//            Customer customer = new Customer();
//            customer.setUsername("johnSmile");
//            customer.setPassword("123");
//            customer.setEmail("denis2@yahoo.com");
//            customer.setRole("CUSTOMER");
//            customer.setCompany(company);
//            customer.setName("Denis");
//            customer.setPhone("123456");
//            customerRepo.saveAndFlush(customer);

//            LoginUserRequest loginUserRequest = LoginUserRequest.builder().email("jhon@yahoo.com").password("123").build();
//            userServiceCommandImpl.deleteUser(loginUserRequest);


//            UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().email("floredenis907@yahoo.com").password("parolaNoua123")
//                    .newUser(User.builder().email("floredenis907@yahoo.com").password("parolaNoua123").username("denisflore120").build()).build();
//            userServiceCommandImpl.updateUser(updateUserRequest);


        };
    }
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://online-school-client.eu-west-1.elasticbeanstalk.com/","http://localhost:4200","http://localhost"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
//                "Access-Control-Request-Method", "Access-Control-Request-Headers","Access-Control-Allow-Headers"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
//                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }
}
