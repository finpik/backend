package com.loanpick.auth.oauth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CustomAuthenticationEntryPointTest {

    private final CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException exception;

    @DisplayName("인증중 서버 에러시 500 상태 코드와 에러 메시지를 반환한다.")
    @Test
    void commence() throws IOException {
        // given
        when(exception.getMessage()).thenReturn("Unauthorized");

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        entryPoint.commence(request, response, exception);

        // then
        assertAll(
            () -> verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
            () -> verify(response).setContentType(HttpHeaderValues.APPLICATION_JSON),
            () -> verify(writer).write(ErrorMessage.AUTHORIZATION_SERVER_ERROR));
    }
}
