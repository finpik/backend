package finpik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"finpik.config", "finpik.kafka", // consumer, producer
        "finpik.db", // entity
        "finpik.redis" // 포함되는 경우
})
public class InfraTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfraTestApplication.class, args);
    }
}
