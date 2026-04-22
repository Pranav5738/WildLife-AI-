package com.example.wildlife.config;

import com.example.wildlife.model.User;
import com.example.wildlife.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoUserInitializer {

    @Bean
    CommandLineRunner seedDemoUser(
            UserRepository userRepository,
            @Value("${app.demo-user.name}") String name,
            @Value("${app.demo-user.email}") String email,
            @Value("${app.demo-user.password}") String password) {
        return args -> {
            if (userRepository.findByEmail(email).isEmpty()) {
                userRepository.save(new User(name, email, password));
            }
        };
    }
}