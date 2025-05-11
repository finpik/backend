package com.loanpick.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${fin-pick.external.recommend-ml.base-url}")
    private String recommendBaseUrl;

    @Bean
    public WebClient recommendWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(recommendBaseUrl).build();
    }
}
