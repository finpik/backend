package com.loanpick.auth.oauth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanpick.auth.oauth.handler.OAuth2SuccessHandler;
import com.loanpick.auth.oauth.handler.reponse.OAuth2Response;
import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.auth.oauth.service.AuthService;
import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;
import com.loanpick.redis.service.CustomRedisService;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OAuth2SuccessHandlerTest {

    @InjectMocks
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomOAuth2User oAuth2User;

    @Mock
    private CustomRedisService customRedisService;

    @Mock
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        oAuth2SuccessHandler = new OAuth2SuccessHandler(jwtProvider, userRepository, customRedisService, objectMapper,
                authService);
    }

    @DisplayName("로그인 성공 시 JWT를 바디에 포함하여 응답한다.")
    @Test
    void addJwtToHeader() throws IOException {
        // given
        User user = User.builder().id(1L).email("user@test.com").username("loanpick").build();

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getEmail()).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtProvider.createAccessToken(any())).thenReturn("test.jwt.token");

        // mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // flush writer to finalize string
        printWriter.flush();

        // then
        String expectedJson = new ObjectMapper().writeValueAsString(Map.of("accessToken", "test.jwt.token"));
        assertAll(() -> verify(response).setStatus(HttpServletResponse.SC_OK),
                () -> verify(response).setContentType("application/json"),
                () -> assertEquals(expectedJson, stringWriter.toString()));
    }

    @DisplayName("사용자가 존재하지 않으면 회원가입 유도 응답을 보낸다.")
    @Test
    void returnsSignUpNeeded() throws IOException {
        // given
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getEmail()).thenReturn("not_exist_user@test.com");
        when(oAuth2User.getUserId()).thenReturn("4848484");
        when(oAuth2User.getProvider()).thenReturn("kakao");
        when(userRepository.findByEmail("not_exist_user@test.com")).thenReturn(Optional.empty());

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        OAuth2Response oAuth2Response = OAuth2Response.builder()
            .id(oAuth2User.getUserId())
            .provider(oAuth2User.getProvider())
            .build();

        String json = objectMapper.writeValueAsString(oAuth2Response);

        // when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        assertAll(
            () -> verify(response).setStatus(HttpServletResponse.SC_OK),
            () -> verify(response).setContentType("application/json"),
            () -> verify(response).setCharacterEncoding("UTF-8"),
            () -> verify(writer).write(json)
        );
    }
}
