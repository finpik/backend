package finpik.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private String recommendBaseUrl;

    @Bean
    public WebClient recommendWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(recommendBaseUrl).build();
    }
}
