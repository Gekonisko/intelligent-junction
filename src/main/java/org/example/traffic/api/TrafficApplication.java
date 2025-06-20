package org.example.traffic.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrafficApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(TrafficApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application running. Press Enter to exit.");
        System.in.read();
    }
}
