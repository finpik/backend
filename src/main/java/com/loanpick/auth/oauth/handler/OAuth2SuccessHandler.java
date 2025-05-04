package com.loanpick.auth.oauth.handler;

import static com.loanpick.util.Values.ACCESS_TOKEN;
import static com.loanpick.util.Values.FIFTEEN_MINUTE_MILL;
import static com.loanpick.util.Values.FOURTEEN_DAYS_SEC;
import static com.loanpick.util.Values.GRAPHQL_URL;
import static com.loanpick.util.Values.REFRESH_TOKEN;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanpick.auth.oauth.constants.HttpHeaderValues;
import com.loanpick.auth.oauth.handler.reponse.OAuth2Response;
import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.auth.oauth.service.AuthService;
import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;
import com.loanpick.redis.service.CustomRedisService;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final CustomRedisService customRedisService;
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String oauthUserId = oAuth2User.getUserId();
        String provider = oAuth2User.getProvider();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            String accessToken = getAccessToken(user.get());
            String refreshToken = getRefreshToken(user.get());
            authService.saveRefreshToken(user.get().getId(), refreshToken);

            responseSuccessLogin(response, accessToken, refreshToken);
        } else {
            saveOAuthAuthenticationToRedis(oauthUserId, email, provider);
            responseSignUpUser(response, oauthUserId, provider);
        }
    }

    private void saveOAuthAuthenticationToRedis(String oauthUserId, String data, String provider) {
        customRedisService.saveEmailForSignUp(oauthUserId, provider, data, Duration.ofMillis(FIFTEEN_MINUTE_MILL));
    }

    private String getAccessToken(User user) {
        return jwtProvider.createAccessToken(user);
    }

    private String getRefreshToken(User user) {
        return jwtProvider.createRefreshToken(user);
    }

    private void responseSuccessLogin(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(HttpHeaderValues.APPLICATION_JSON);
        response.setCharacterEncoding(HttpHeaderValues.UTF_8);

        setRefreshTokenOnCookie(response, refreshToken);

        String body = objectMapper.writeValueAsString(Map.of(ACCESS_TOKEN, accessToken));
        response.getWriter().write(body);
    }

    private void setRefreshTokenOnCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath(GRAPHQL_URL);
        refreshTokenCookie.setMaxAge(FOURTEEN_DAYS_SEC);
        response.addCookie(refreshTokenCookie);
    }

    private void responseSignUpUser(HttpServletResponse response, String id, String provider) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(HttpHeaderValues.APPLICATION_JSON);
        response.setCharacterEncoding(HttpHeaderValues.UTF_8);

        OAuth2Response oAuth2Response = OAuth2Response.builder().id(id).provider(provider).build();
        String json = objectMapper.writeValueAsString(oAuth2Response);

        response.getWriter().write(json);
    }
}
