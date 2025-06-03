package finpik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"finpik.config", "finpik.consumer", "finpik.producer", "finpik.redis",
        "finpik.jpa.repository",})
public class InfraTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfraTestApplication.class, args);
    }
}
