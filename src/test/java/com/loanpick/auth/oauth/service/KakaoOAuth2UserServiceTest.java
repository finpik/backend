package com.loanpick.auth.oauth.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;

@ExtendWith(MockitoExtension.class)
class KakaoOAuth2UserServiceTest {
  @InjectMocks KakaoOAuth2UserService kakaoOAuth2UserService;

  @Mock private OAuth2UserRequest userRequest;

  @DisplayName("loadUser 호출시 CustomOAuth2User 클래스가 나오고 attribute에서 가져온 email 값이 채워진다.")
  @Test
  void loadUser() {
    // given
    Map<String, Object> attributes =
        Map.of(
            "id",
            "12345",
            "kakao_account",
            Map.of("email", "test@kakao.com", "profile", Map.of("nickname", "홍길동")));

    CustomOAuth2User fakeUser =
        new CustomOAuth2User(attributes, "test@kakao.com", "kakao", "12345");
    KakaoOAuth2UserService spyService = Mockito.spy(kakaoOAuth2UserService);
    doReturn(fakeUser).when(spyService).loadUser(userRequest);

    // when
    OAuth2User result = spyService.loadUser(userRequest);
    CustomOAuth2User customOAuth2User = (CustomOAuth2User) result;

    // then
    assertAll(
        () -> assertInstanceOf(CustomOAuth2User.class, result),
        () -> assertEquals("test@kakao.com", customOAuth2User.getEmail()),
        () -> assertNotNull(customOAuth2User.getAttributes()));
  }
}
