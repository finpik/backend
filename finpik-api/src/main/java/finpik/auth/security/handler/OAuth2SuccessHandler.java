package finpik.auth.security.handler;

import static finpik.util.Values.FIFTEEN_MINUTE_MILL;
import static finpik.util.Values.FOURTEEN_DAYS_MILL;
import static finpik.util.Values.FOURTEEN_DAYS_SEC;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import finpik.entity.User;
import finpik.repository.auth.AuthCacheRepository;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import finpik.JwtProvider;
import finpik.auth.security.user.CustomOAuth2User;
import finpik.dto.CreateTokenDto;
import finpik.repository.auth.AuthRedisRepository;
import finpik.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

//TODO: 리팩토링 대상
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final AuthRedisRepository authRedisRepository;
    private final AuthCacheRepository authCacheRepository;

    private static final String REDIRECT_URI = "http://localhost:3000/sign-in";
    private static final String DEPLOYED_REDIRECT_URI = "https://finpik.vercel.app/sign-in";

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
            authCacheRepository.saveRefreshToken(user.get().getId(), refreshToken, Duration.ofSeconds(FOURTEEN_DAYS_SEC));

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
        Date expiresAt = new Date(issuedAt.getTime() + FOURTEEN_DAYS_MILL);
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
        String redirectUrl = UriComponentsBuilder
            .fromUriString(DEPLOYED_REDIRECT_URI)
            .queryParam("accessToken", accessToken)
            .build()
            .toUriString();

        setRefreshTokenOnCookie(response, refreshToken);
        response.sendRedirect(redirectUrl);
    }

    private void setRefreshTokenOnCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
            .domain("localhost")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void responseSignUpUser(HttpServletResponse response, String id, String provider) throws IOException {
        String redirectUrl = UriComponentsBuilder
            .fromUriString(DEPLOYED_REDIRECT_URI)
            .queryParam("id", id)
            .queryParam("provider", provider)
            .build()
            .toUriString();

        response.sendRedirect(redirectUrl);

//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding(UTF_8);
//
//        OAuth2Response oAuth2Response = OAuth2Response.builder().id(id).provider(provider).build();
//        String json = objectMapper.writeValueAsString(oAuth2Response);
//
//        response.getWriter().write(json);
    }
}
