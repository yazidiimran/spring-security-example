package com.example.demo.controller;


import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RefreshRequest;
import com.example.demo.entity.CustomUser;
import com.example.demo.repo.CustomUserRepo;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final CustomUserRepo repo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/public/hello")
    public String hello(){
        return "Hello from public";
    }

    @GetMapping("/users/hello")
    public String helloUser(){
        return "Hello from user";
    }

    @GetMapping("/admin/hello")
    public String helloAdmin(){
        return "Hello from Admin";
    }

    @GetMapping("/other-admin/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloOtherAdmin(){
        return "Hello from orher Admin";
    }

    @GetMapping("/users/{id}")
    @PostAuthorize("returnObject.username == authentication.name")
    public CustomUser getUser(@PathVariable Long id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("User not found !"));
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody AuthRequest request){
        authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(request.username(),request.password()));
        var accessToken = jwtService.generateToken(request.username());
        var refreshtoken = refreshTokenService.generateToken(request.username());
        return new AuthResponse(accessToken,refreshtoken);
    }

    @PostMapping("auth/refresh")
    public AuthResponse refersh(@RequestBody RefreshRequest request){
        String username = refreshTokenService.verifyToken(request.refreshtoken());
        String accessToken = jwtService.generateToken(username);
        String newRefreshToken = refreshTokenService.generateToken(username);
        refreshTokenService.delete(request.refreshtoken());
        return new AuthResponse(accessToken,newRefreshToken);
    }
}


