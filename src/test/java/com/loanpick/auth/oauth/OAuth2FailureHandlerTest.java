package com.loanpick.auth.oauth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import com.loanpick.auth.oauth.handler.OAuth2FailureHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class OAuth2FailureHandlerTest {
    @InjectMocks
    private OAuth2FailureHandler oAuth2FailureHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException exception;

    @DisplayName("인증 실패 시 401 상태 코드와 에러 메시지를 반환한다.")
    @Test
    void onAuthenticationFailure() throws IOException {
        // given
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        oAuth2FailureHandler.onAuthenticationFailure(request, response, exception);

        // then
        assertAll(() -> verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED),
                () -> verify(response).setContentType(HttpHeaderValues.APPLICATION_JSON),
                () -> verify(writer).write(ErrorMessage.AUTHORIZATION_FAIL));
    }
}
