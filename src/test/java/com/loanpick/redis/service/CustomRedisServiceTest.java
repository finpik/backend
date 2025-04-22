package com.loanpick.redis.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomRedisServiceTest {
    @Autowired
    CustomRedisService customRedisService;

    @DisplayName("userId와 provider를 가지고 AceesToken을 저장한 것을 조회할 수 있다.")
    @Test
    void saveAuthenticationForSignUp() {
        //given
        String userId = "userId";
        String providerId = "providerId";
        String accessToken = "accessToken";

        //when
        customRedisService.saveAuthenticationForSignUp(userId, providerId, accessToken, Duration.ofSeconds(10));
        String savedAccessToken = customRedisService.getAuthenticationForSignUp(userId, providerId);

        //then
        assertEquals(accessToken, savedAccessToken);
    }
}
