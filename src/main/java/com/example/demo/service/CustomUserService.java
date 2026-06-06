package com.example.demo.service;

import com.example.demo.entity.CustomUser;
import com.example.demo.repo.CustomUserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {

    private final CustomUserRepo repo;

    public CustomUserService(CustomUserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = repo.findByUsername(username).orElseThrow(()->new RuntimeException("user not found !"));

        return User
                .withUsername(customUser.getUsername())
                .password((customUser.getPassword()))
                .roles(customUser.getRoles())
                .build();
    }
}
