//package finpik.auth.security.handler;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.Duration;
//import java.util.Optional;
//
//import finpik.entity.User;
//import finpik.repository.auth.AuthCacheRepository;
//import finpik.util.Values;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.core.Authentication;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import finpik.JwtProvider;
//import finpik.auth.security.user.CustomOAuth2User;
//import finpik.repository.auth.AuthRedisRepository;
//import finpik.repository.user.UserRepository;
//import jakarta.servlet.http.Cookie;
//
//@ExtendWith(MockitoExtension.class)
//class OAuth2SuccessHandlerTest {
//
//    @InjectMocks
//    private OAuth2SuccessHandler successHandler;
//
//    @Mock
//    private JwtProvider jwtProvider;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AuthRedisRepository authRedisRepository;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private AuthCacheRepository authCacheRepository;
//
//    @DisplayName("기존 사용자일 경우 accessToken, refreshToken 생성 및 응답 반환")
//    @Test
//    void existingUser_accessTokenRefreshTokenResponse() throws Exception {
//        // given
//        CustomOAuth2User userPrincipal = mock(CustomOAuth2User.class);
//        when(userPrincipal.getEmail()).thenReturn("test@example.com");
//
//        Authentication auth = mock(Authentication.class);
//        when(auth.getPrincipal()).thenReturn(userPrincipal);
//
//        User user = mock(User.class);
//        when(user.getId()).thenReturn(1L);
//        when(user.getEmail()).thenReturn("test@example.com");
//
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//        when(jwtProvider.createAccessToken(any())).thenReturn("access-token");
//        when(jwtProvider.createRefreshToken(any())).thenReturn("refresh-token");
//        when(objectMapper.writeValueAsString(any())).thenReturn("{\"accessToken\":\"access-token\"}");
//
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        // when
//        successHandler.onAuthenticationSuccess(new MockHttpServletRequest(), response, auth);
//
//        // then
//        assertThat(response.getStatus()).isEqualTo(200);
//        assertThat(response.getContentAsString()).contains("access-token");
//        Cookie cookie = response.getCookie("refreshToken");
//        assertThat(cookie).isNotNull();
//        assertThat(cookie.isHttpOnly()).isTrue();
//        assertThat(cookie.getValue()).isEqualTo("refresh-token");
//
//        verify(authCacheRepository)
//            .saveRefreshToken(eq(1L), eq("refresh-token"), eq(Duration.ofSeconds(Values.FOURTEEN_DAYS_SEC)));
//    }
//
//    @DisplayName("신규 사용자일 경우 Redis에 임시 저장 후 회원가입 응답 반환")
//    @Test
//    void newUser_saveToRedis_andReturnSignUpResponse() throws Exception {
//        // given
//        CustomOAuth2User userPrincipal = mock(CustomOAuth2User.class);
//        when(userPrincipal.getEmail()).thenReturn("new@example.com");
//        when(userPrincipal.getUserId()).thenReturn("oauth-user-id");
//        when(userPrincipal.getProvider()).thenReturn("kakao");
//
//        Authentication auth = mock(Authentication.class);
//        when(auth.getPrincipal()).thenReturn(userPrincipal);
//
//        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
//        when(objectMapper.writeValueAsString(any())).thenReturn("{\"id\":\"oauth-user-id\",\"provider\":\"kakao\"}");
//
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        // when
//        successHandler.onAuthenticationSuccess(new MockHttpServletRequest(), response, auth);
//
//        // then
//        assertThat(response.getStatus()).isEqualTo(200);
//        assertThat(response.getContentAsString()).contains("oauth-user-id");
//        assertThat(response.getContentAsString()).contains("kakao");
//
//        verify(authRedisRepository).saveEmailForSignUp(eq("oauth-user-id"), eq("kakao"), eq("new@example.com"), any());
//    }
//}
