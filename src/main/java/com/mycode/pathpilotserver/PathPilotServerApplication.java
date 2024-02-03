package com.mycode.pathpilotserver;

import com.mycode.pathpilotserver.customers.repository.CustomerRepo;
import com.mycode.pathpilotserver.driver.repository.DriverRepo;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipmentDetails.repository.ShipmentDetailsRepo;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
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
    CommandLineRunner commandLineRunner(UserRepo userRepo , CustomerRepo customerRepo,
                                        VehicleRepo vehicleRepo, ShipmentRepo shipmentRepo, ShipmentDetailsRepo shipmentDetailsRepo,
                                        DriverRepo driverRepo, OrderRepo orderRepo
                                        ) {
        return args -> {

            User user = new User();
            user.setEmail(" Email Test");
            user.setPassword("Password Test");
            user.setRole("Role Test");
            user.setUsername("Username Test");
            user.setId(1L);
            userRepo.saveAndFlush(user);

           User x=  userRepo.findByEmail(" Email Test").get();
           System.out.println(x.toString());

        };
    }

}
