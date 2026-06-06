package com.example.demo.config;


import com.example.demo.entity.CustomUser;
import com.example.demo.repo.CustomUserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class InDBUser implements CommandLineRunner {

    private final CustomUserRepo repo;

    private final PasswordEncoder passwordEncoder;

    public InDBUser(CustomUserRepo repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        CustomUser user = CustomUser.builder()
                .username("imran")
                .password(passwordEncoder.encode("imran"))
                .roles("USER")
                .build();

        CustomUser admin = CustomUser.builder()
                .username("khalid")
                .password(passwordEncoder.encode("khalid"))
                .roles("ADMIN")
                .build();

        System.out.println("Users created: "+repo.saveAll(List.of(user,admin)).size());
    }
}
