package com.loanpick.auth.oauth.service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomOAuth2UserTest {

    @DisplayName("CustomOAuth2User 제대로 객체가 생성된다.")
    @Test
    void customOAuth2User() {
        // given
        Map<String, Object> attributes = Map.of("id", "12345", "kakao_account",
                Map.of("email", "test@kakao.com", "profile", Map.of("nickname", "홍길동")));

        String email = "test@kakao.com";
        String provider = "kakao";
        String userId = "12345";
        String accessToken = "accessToken";

        // when
        CustomOAuth2User user = new CustomOAuth2User(attributes, email, provider, userId, accessToken);

        // then
        assertEquals(email, user.getEmail());
    }
}
