package com.example.hrinterviewsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HrInterviewSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrInterviewSystemApplication.class, args);
    }
//    @Bean
//    public CommandLineRunner test(
//            PasswordEncoder passwordEncoder
//    ) {
//        return args -> {
//
//            System.out.println(
//                    passwordEncoder.encode("1")
//            );
//
//        };
//    }
}

