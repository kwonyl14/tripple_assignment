package com.tripple.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TrippleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrippleApplication.class, args);
    }

}
