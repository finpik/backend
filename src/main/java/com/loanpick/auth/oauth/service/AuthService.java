package com.loanpick.auth.oauth.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.loanpick.redis.service.AuthRedisService;
import com.loanpick.util.Values;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRedisService authRedisService;

    public boolean isValid(long userId, String refreshToken) {
        return authRedisService.isRefreshTokenValid(userId, refreshToken);
    }

    public void saveRefreshToken(long userId, String refreshToken) {
        authRedisService.saveRefreshToken(userId, refreshToken, Duration.ofMillis(Values.FOURTEEN_DAYS_MILL));
    }
}
