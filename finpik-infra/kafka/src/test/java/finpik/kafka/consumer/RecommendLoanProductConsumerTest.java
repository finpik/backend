package finpik.kafka.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import finpik.InfraTestApplication;
import finpik.entity.history.dlq.KafkaFailedMessage;
import finpik.jpa.repository.history.dlq.KafkaFailedMessageJpaRepository;
import finpik.kafka.config.KafkaTestConfig;

@SpringBootTest(classes = InfraTestApplication.class)
@Import(KafkaTestConfig.class)
@EmbeddedKafka(partitions = 1, topics = {"recommendation-results", "recommendations-dlt"})
@DirtiesContext
@ActiveProfiles("test")
class RecommendLoanProductConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaFailedMessageJpaRepository kafkaFailedMessageJpaRepository;

    @DisplayName("DLQ 핸들러 테스트: 3번의 시도 후 에러 발생시 실패 메세지를 카프카에 보낸 후 메세지를 저장한다")
    @Test
    void testDlqHandling_whenMessageFails() throws InterruptedException {
        String failingMessage = "{\"profileId\": 99, \"message\": \"fail\"}";

        kafkaTemplate.send("recommendation-results", failingMessage);

        Thread.sleep(10_000);

        List<KafkaFailedMessage> failed = kafkaFailedMessageJpaRepository.findAll();
        assertThat(failed).anySatisfy(msg -> assertThat(msg.getPayload()).contains("{\"profileId\":99,\"eventId\":99}"));
    }
}
