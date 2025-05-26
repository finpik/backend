package finpik.auth.security.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import finpik.error.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class OAuth2FailureHandlerTest {

    private final OAuth2FailureHandler failureHandler = new OAuth2FailureHandler();

    @DisplayName("OAuth2 인증 실패 시 401과 메시지를 반환한다")
    @Test
    void onAuthenticationFailure() throws Exception {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new BadCredentialsException("Invalid login");

        // When
        failureHandler.onAuthenticationFailure(request, response, authException);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getContentAsString()).isEqualTo(ErrorCode.OAUTH2_AUTHORIZATION_FAIL.getMessage());
    }
}
