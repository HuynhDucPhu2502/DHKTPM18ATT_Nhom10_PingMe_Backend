package me.huynhducphu.PingMe_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableWebSocketMessageBroker
public class PingMeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PingMeBackendApplication.class, args);
    }

}
