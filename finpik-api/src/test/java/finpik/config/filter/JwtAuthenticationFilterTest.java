package finpik.config.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import finpik.jwt.JwtProvider;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserService userService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = mock(FilterChain.class);
    }

    @DisplayName("토큰이 유효하다면 request attribute에 user가 들어있다.")
    @Test
    void validAccessToken() throws Exception {
        // given
        String token = "testToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        given(jwtProvider.isValid(token)).willReturn(true);
        given(jwtProvider.getUserId(token)).willReturn(1L);
        User mockUser = User.builder().id(1L).email("test@user.com").build();
        given(userService.findUserBy(1L)).willReturn(mockUser);

        // when
        filter.doFilterInternal(request, response, chain);
        User result = (User) request.getAttribute("user");

        // then
        assertAll(() -> assertThat(result).isNotNull(), () -> assertThat(result.getId()).isEqualTo(1L));
    }

    @DisplayName("토큰이 유효하지 않다면 request attribute에 user가 들어있지 않다.")
    @Test
    void invalidAccessToken() throws Exception {
        // givenm
        String token = "invalidToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        given(jwtProvider.isValid(token)).willReturn(false);

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        assertThat(request.getAttribute("user")).isNull();
    }

    @DisplayName("Refresh Token이 존재한다면 Cookie에 들어있다.")
    @Test
    void refreshTokenSetInCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("refreshToken", "testRefreshToken");
        request.setCookies(cookie);

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        assertThat(request.getAttribute("refreshToken")).isEqualTo("testRefreshToken");
    }

    @DisplayName("Header에 아무값이 없다면 Cookie에 아무 값도 세팅되지 않는다.")
    @Test
    void nothingSetInCookie() throws Exception {
        // given
        // when
        filter.doFilterInternal(request, response, chain);

        // then
        assertThat(request.getAttribute("user")).isNull();
        assertThat(request.getAttribute("refreshToken")).isNull();
    }
}
