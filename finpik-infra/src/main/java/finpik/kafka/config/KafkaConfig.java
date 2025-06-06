// package finpik.kafka.config;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.clients.producer.ProducerConfig;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.apache.kafka.common.serialization.StringSerializer;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.annotation.EnableKafka;
// import
// org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
// import org.springframework.kafka.core.*;
//
// import java.util.HashMap;
// import java.util.Map;
//
// @Slf4j
// @EnableKafka
// @Configuration
// public class KafkaConfig {
//
// @Value("${spring.kafka.bootstrap-servers}")
// private String bootstrapServers;
//
// @Value("${spring.kafka.consumer.group-id}")
// private String groupId;
//
// @Value("${spring.kafka.consumer.auto-offset-reset}")
// private String autoOffsetReset = "earliest";
//
// // Consumer 설정
// @Bean
// public ConsumerFactory<String, String> consumerFactory() {
// log.info("Creating Kafka consumer factory: {}", bootstrapServers);
//
// Map<String, Object> props = new HashMap<>();
// props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
// props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
// props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
// StringDeserializer.class);
// props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
// StringDeserializer.class);
// props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
// return new DefaultKafkaConsumerFactory<>(props);
// }
//
// @Bean
// public ConcurrentKafkaListenerContainerFactory<String, String>
// kafkaListenerContainerFactory() {
// ConcurrentKafkaListenerContainerFactory<String, String> factory =
// new ConcurrentKafkaListenerContainerFactory<>();
// factory.setConsumerFactory(consumerFactory());
// return factory;
// }
//
// // Producer 설정
// @Bean
// public ProducerFactory<String, Object> producerFactory() {
// Map<String, Object> configProps = new HashMap<>();
// configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
// configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
// StringSerializer.class);
// configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
// StringSerializer.class);
// return new DefaultKafkaProducerFactory<>(configProps);
// }
//
// @Bean
// public KafkaTemplate<String, Object> kafkaTemplate() {
// return new KafkaTemplate<>(producerFactory());
// }
// }
//
