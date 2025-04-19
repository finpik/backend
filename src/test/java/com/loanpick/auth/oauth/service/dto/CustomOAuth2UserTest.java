package com.loanpick.auth.oauth.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomOAuth2UserTest {

    @DisplayName("CustomOAuth2User 제대로 객체가 생성된다.")
    @Test
    void customOAuth2User() {
        //given
        Map<String, Object> attributes = Map.of(
            "id", "12345",
            "kakao_account", Map.of(
                "email", "test@kakao.com",
                "profile", Map.of("nickname", "홍길동")
            )
        );

        String email = "test@kakao.com";
        String provider = "kakao";

        //when
        CustomOAuth2User user = new CustomOAuth2User(attributes, email, provider, "12345");

        //then
        assertEquals(email, user.getEmail());
    }
}
