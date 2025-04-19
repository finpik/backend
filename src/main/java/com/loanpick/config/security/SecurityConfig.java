package com.loanpick.config.security;

import com.loanpick.auth.oauth.OAuth2SuccessHandler;
import com.loanpick.auth.oauth.service.KakaoOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        "/login/oauth2/**",        // 카카오 콜백 URL
                        "/oauth2/**",              // 인가 시작 URL (`/oauth2/authorization/kakao`)
                        "/auth/**",                // 사용자 정의 callback 또는 API
                        "/favicon.ico", "/css/**", "/js/**" // 정적 리소스
                    ).permitAll()
                    .anyRequest().permitAll()
            ).oauth2Login(oauth ->
                oauth.userInfoEndpoint(user ->
                        user.userService(kakaoOAuth2UserService)
                    ).successHandler(oAuth2SuccessHandler)
            );

        return http.build();
    }
}
