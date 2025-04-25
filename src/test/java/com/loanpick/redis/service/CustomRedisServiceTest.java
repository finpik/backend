package com.loanpick.redis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CustomRedisServiceTest {
    @Autowired
    CustomRedisService customRedisService;

    @DisplayName("userId와 provider를 가지고 AccessToken을 저장한 것을 조회할 수 있다.")
    @Test
    void saveEmailForSignUp() {
        // given
        String userId = "userId";
        String providerId = "providerId";
        String accessToken = "accessToken";

        // when
        customRedisService.saveEmailForSignUp(userId, providerId, accessToken, Duration.ofSeconds(10));
        String savedAccessToken = customRedisService.getEmailByCustomId(userId, providerId);

        // then
        assertEquals(accessToken, savedAccessToken);
    }
}
