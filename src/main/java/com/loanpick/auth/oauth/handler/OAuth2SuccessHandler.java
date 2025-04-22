package com.loanpick.auth.oauth.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanpick.auth.oauth.HttpHeaderValues;
import com.loanpick.auth.oauth.handler.reponse.OAuth2Response;
import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;
import com.loanpick.redis.service.CustomRedisService;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;

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
    private static final int FIVE_MINUTE = 300_000;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String oauthUserId = oAuth2User.getUserId();
        String provider = oAuth2User.getProvider();
        String accessToken = oAuth2User.getAccessToken();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            responseSuccessLogin(response, getJwtToken(user.get()));
        } else {
            saveOAuthAuthenticationToRedis(oauthUserId, accessToken, provider);
            responseSignUpUser(response, oauthUserId, provider);
        }
    }

    private void saveOAuthAuthenticationToRedis(String oauthUserId, String data, String provider) {
        customRedisService.saveAuthenticationForSignUp(oauthUserId, provider, data, Duration.ofMinutes(FIVE_MINUTE));
    }

    private String getJwtToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + FIVE_MINUTE);
        return jwtProvider.createToken(user, now, expiry);
    }

    private void responseSuccessLogin(HttpServletResponse response, String jwt) {
        // JSON 응답
        response.setHeader(HttpHeaderValues.AUTHORIZATION, HttpHeaderValues.BEARER + jwt);
        response.setStatus(HttpServletResponse.SC_OK);
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
