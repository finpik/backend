package com.loanpick.redis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.LoanPickApplication;

@SpringBootTest(classes = LoanPickApplication.class)
@ActiveProfiles("test")
class AuthRedisServiceTest {
    @Autowired
    AuthRedisService authRedisService;

    @DisplayName("userId와 provider를 가지고 AccessToken을 저장한 것을 조회할 수 있다.")
    @Test
    void saveEmailForSignUp() {
        // given
        String userId = "userId";
        String providerId = "providerId";
        String accessToken = "accessToken";

        // when
        authRedisService.saveEmailForSignUp(userId, providerId, accessToken, Duration.ofSeconds(10));
        String savedAccessToken = authRedisService.getEmailByCustomId(userId, providerId);

        // then
        assertEquals(accessToken, savedAccessToken);
    }
}
