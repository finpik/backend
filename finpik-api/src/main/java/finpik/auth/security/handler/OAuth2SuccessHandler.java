package finpik.auth.security.handler;

import static finpik.util.Values.ACCESS_TOKEN;
import static finpik.util.Values.FIFTEEN_MINUTE_MILL;
import static finpik.util.Values.FOURTEEN_DAYS_MILL;
import static finpik.util.Values.FOURTEEN_DAYS_SEC;
import static finpik.util.Values.GRAPHQL_URL;
import static finpik.util.Values.REFRESH_TOKEN;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import finpik.JwtProvider;
import finpik.auth.security.handler.reponse.OAuth2Response;
import finpik.auth.security.user.CustomOAuth2User;
import finpik.auth.service.AuthService;
import finpik.dto.CreateTokenDto;
import finpik.redis.repository.auth.AuthRedisRepository;
import finpik.user.entity.User;
import finpik.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final AuthRedisRepository authRedisRepository;
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    private static final String UTF_8 = "UTF-8";

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
        authRedisRepository.saveEmailForSignUp(oauthUserId, provider, data, Duration.ofMillis(FIFTEEN_MINUTE_MILL));
    }

    private String getAccessToken(User user) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + FIFTEEN_MINUTE_MILL);
        CreateTokenDto dto = CreateTokenDto.builder().userId(user.getId()).email(user.getEmail()).issuedAt(issuedAt)
                .expiration(expiresAt).build();

        return jwtProvider.createAccessToken(dto);
    }

    private String getRefreshToken(User user) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + FOURTEEN_DAYS_MILL);
        CreateTokenDto dto = CreateTokenDto.builder().userId(user.getId()).email(user.getEmail()).issuedAt(issuedAt)
                .expiration(expiresAt).build();

        return jwtProvider.createRefreshToken(dto);
    }

    private void responseSuccessLogin(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);

        setRefreshTokenOnCookie(response, refreshToken);

        String body = objectMapper.writeValueAsString(Map.of(ACCESS_TOKEN, accessToken));
        response.getWriter().write(body);
    }

    private void setRefreshTokenOnCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath(GRAPHQL_URL);
        refreshTokenCookie.setMaxAge(FOURTEEN_DAYS_SEC);
        response.addCookie(refreshTokenCookie);
    }

    private void responseSignUpUser(HttpServletResponse response, String id, String provider) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);

        OAuth2Response oAuth2Response = OAuth2Response.builder().id(id).provider(provider).build();
        String json = objectMapper.writeValueAsString(oAuth2Response);

        response.getWriter().write(json);
    }
}
