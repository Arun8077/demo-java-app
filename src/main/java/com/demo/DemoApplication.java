package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        int unused = 42;   // ❌ intentional unused variable
        return "Hello from GKE demo! We are doing POC on a scalable application over gke";
    }

    @GetMapping("/healthz")
    public String health() {
        return "OK";
    }
}
