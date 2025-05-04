package com.loanpick.auth.oauth.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.loanpick.redis.service.CustomRedisService;
import com.loanpick.util.Values;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomRedisService customRedisService;

    public boolean isValid(long userId, String refreshToken) {
        return customRedisService.isRefreshTokenValid(userId, refreshToken);
    }

    public void saveRefreshToken(long userId, String refreshToken) {
        customRedisService.saveRefreshToken(userId, refreshToken, Duration.ofMillis(Values.FOURTEEN_DAYS_MILL));
    }
}
