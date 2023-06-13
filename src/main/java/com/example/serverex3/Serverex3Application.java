package com.example.serverex3;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.CountDownLatch;

import static com.example.serverex3.Loggin.createLogFolder;

@SpringBootApplication
public class Serverex3Application {

    public static void main(String[] args) {
        createLogFolder();
        SpringApplication.run(Serverex3Application.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {

        return args -> {
            CountDownLatch latch = new CountDownLatch(1);
            System.out.println("Application is running. Press Ctrl+C to terminate...");
            latch.await();
        };

    }
}

