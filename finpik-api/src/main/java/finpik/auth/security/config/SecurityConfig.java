package finpik.auth.security.config;

import java.util.List;

import finpik.auth.security.oauth.OAuth2AuthorizationRequestCookieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import finpik.auth.security.entrypoint.CustomAuthenticationEntryPoint;
import finpik.auth.security.handler.OAuth2FailureHandler;
import finpik.auth.security.handler.OAuth2SuccessHandler;
import finpik.auth.security.oauth.kakao.KakaoOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    @Value("${fin-pik.domain.domain_url}")
    private String domainUrl;
    @Value("${fin-pik.domain.front_test_url}")
    private String frontTestUrl;
    @Value("${fin-pik.domain.front_app_url}")
    private String frontAppUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.requestMatchers(
                    "/login/oauth2/**", // 카카오 콜백 URL
                    "/oauth2/**", // 인가 시작 URL (`/oauth2/authorization/kakao`)
                    "/auth/**", // 사용자 정의 callback 또는 API
                    "/graphql",
                    "/actuator/prometheus", "/actuator/health", "/actuator/info")
                .permitAll()
                .anyRequest()
                .permitAll()
            ).oauth2Login(oauth -> oauth
                .userInfoEndpoint(user -> user.userService(kakaoOAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .authorizationEndpoint(a ->
                    a.authorizationRequestRepository(new OAuth2AuthorizationRequestCookieRepository())
                )
            )
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(authenticationEntryPoint)
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontTestUrl, domainUrl, frontAppUrl));
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://finpik.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
