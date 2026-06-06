package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    public String generateToken(String username) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        var token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        //var token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,username, Duration.ofDays(7));
        return token;
    }

    public String verifyToken(String refreshtoken) {
        String username = redisTemplate.opsForValue().get(refreshtoken);
        if(username==null){
            throw new RuntimeException("Refresh token invalid !");
        }
        return username;
    }

    public void delete(String refreshtoken) {
        redisTemplate.delete(refreshtoken);
    }
}
