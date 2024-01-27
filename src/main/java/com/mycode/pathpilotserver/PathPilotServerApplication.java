package com.mycode.pathpilotserver;

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
    CommandLineRunner commandLineRunner() {
        return args -> {


        };
    }

}
