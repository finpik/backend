package com.loanpick.auth.oauth.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class KakaoUserInfoTest {

    @DisplayName("kakao user로 되어 있는 user info에서 알맞은 email을 뽑아낼 수 있다.")
    @Test
    void test() {
        // given
        Map<String, Object> attributes = Map.of("id", 12345L, "kakao_account",
                Map.of("email", "test@kakao.com", "profile", Map.of("nickname", "홍길동")));

        // when
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        // then
        assertEquals("test@kakao.com", kakaoUserInfo.getEmail());
    }
}
