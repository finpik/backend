package finpik.kafka.config;

import java.util.Map;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@TestConfiguration
public class KafkaTestConfig {
    @Bean
    public ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker embeddedKafka) {
        Map<String, Object> configs = KafkaTestUtils.producerProps(embeddedKafka);
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
