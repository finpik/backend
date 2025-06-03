package finpik.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public ScheduledExecutorService kafkaRetryExecutor() {
        return Executors.newScheduledThreadPool(2);
    }
}
