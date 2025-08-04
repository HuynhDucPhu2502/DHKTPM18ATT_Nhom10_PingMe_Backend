package me.huynhducphu.PingMe_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PingMeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PingMeBackendApplication.class, args);
    }

}
