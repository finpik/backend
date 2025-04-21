package com.loanpick.auth.oauth;

import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Captor
    private ArgumentCaptor<String> jwtCaptor;

    @BeforeEach
    void setup() {
        oAuth2SuccessHandler = new OAuth2SuccessHandler(jwtProvider, userRepository);
    }

    @DisplayName("로그인 성공 시 JWT를 헤더에 포함하여 응답한다.")
    @Test
    void successLogin_addJwtToHeader() throws IOException {
        // given
        User user = User.builder()
            .id(1L)
            .email("user@test.com")
            .username("loanpick")
            .build();

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getEmail()).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtProvider.createToken(any(), any(), any())).thenReturn("test.jwt.token");

        // mock response
        doNothing().when(response).setHeader(anyString(), anyString());

        // when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        assertAll(
            () -> verify(response).setHeader("Authorization", "Bearer test.jwt.token"),
            () -> verify(response).setStatus(HttpServletResponse.SC_OK)
        );
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 회원가입 유도 응답을 보낸다.")
    void userNotFound_returnsSignUpNeeded() throws IOException {
        // given
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getEmail()).thenReturn("not_exist_user@test.com");
        when(userRepository.findByEmail("not_exist_user@test.com")).thenReturn(Optional.empty());

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        assertAll(
            () -> verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND),
            () -> verify(response).setContentType("application/json"),
            () -> verify(response).setCharacterEncoding("UTF-8"),
            () -> verify(writer).write(ErrorMessage.SIGN_UP_NEEDED)
        );
    }
}
