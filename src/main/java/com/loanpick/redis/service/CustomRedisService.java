package com.loanpick.redis.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String AUTHENTICATION_PREFIX = "authentication:";
    private static final String COLON = ":";

    public void saveAuthenticationForSignUp(String id, String provider, String data, Duration ttl) {
        String customKey = AUTHENTICATION_PREFIX + id + COLON + provider;
        redisTemplate.opsForValue().set(customKey, data, ttl);
    }

    public String getAuthenticationForSignUp(String id, String provider) {
        String customKey = AUTHENTICATION_PREFIX + id + COLON + provider;;
        return redisTemplate.opsForValue().get(customKey);
    }
}
