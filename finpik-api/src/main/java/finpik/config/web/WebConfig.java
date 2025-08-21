package finpik.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${fin-pik.domain.domain_url}")
    private String domainUrl;
    @Value("${fin-pik.domain.front_test_url}")
    private String frontTestUrl;
    @Value("${fin-pik.domain.front_app_url}")
    private String frontAppUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(frontTestUrl, domainUrl, frontAppUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(true);
    }
}
