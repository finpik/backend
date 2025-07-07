package finpik.consumer;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import finpik.dto.CreateRecommendedLoanProductEvent;
import finpik.entity.history.dlq.KafkaFailedMessage;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.jpa.repository.history.dlq.KafkaFailedMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendLoanProductConsumer {
    private static final String RECOMMENDATION_TOPIC = "recommendation-results";
    private static final String LOAN_RECOMMENDER_GROUP_ID = "loan-recommender";
    private static final String PROFILE_ID = "profileId";
    private static final String EVENT_ID = "eventId";
    private final KafkaFailedMessageJpaRepository kafkaFailedMessageJpaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    /**
     * @param message
     * 기본 설정 시도 3번, dlq 토픽 서픽스 기본 설정 -dlq 사용
     */
    @RetryableTopic(backoff = @Backoff(delay = 1000, multiplier = 2))
    @KafkaListener(topics = RECOMMENDATION_TOPIC, groupId = LOAN_RECOMMENDER_GROUP_ID)
    public void recommendLoanProduct(String message) {
        log.info("[컨슈머가 받은 메세지]: " + message);

        CreateRecommendedLoanProductEvent event = toEvent(message);

        log.info("이벤트: " + event.profileId());

        eventPublisher.publishEvent(event);
    }

    private CreateRecommendedLoanProductEvent toEvent(String message) {
        log.info(message);

        try {
            CreateRecommendedLoanProductEvent createRecommendedLoanProductEvent =
                objectMapper.readValue(message, new TypeReference<>() {});

            createRecommendedLoanProductEvent.validateFields();

            return createRecommendedLoanProductEvent;
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_CONVERT_KAFKA_MESSAGE_LOAN_PRODUCT);
        }
    }

    @DltHandler
    public void handleDlt(String message, ConsumerRecord<?, ?> record) {
        Throwable cause = extractException(record);

        LocalDateTime now = LocalDateTime.now();
        String payload = extractProfileIdOnly(message);

        kafkaFailedMessageJpaRepository.save(KafkaFailedMessage.from(record.topic(), payload, cause.getMessage(),
                KafkaFailedMessage.Status.DLQ_SENT, now, now));

        // TODO(슬랙 메세지 처리 필요)
        // slackNotifier.notifyDlq(record.topic(), message, cause);

        log.warn("DLQ 메시지 처리 완료: {}", message);
    }

    private Throwable extractException(ConsumerRecord<?, ?> record) {
        Headers headers = record.headers();
        String exceptionMessage = Optional.ofNullable(headers.lastHeader("kafka_dlt-exception-message"))
                .map(header -> new String(header.value())).orElse("Unknown");

        String exceptionClass = Optional.ofNullable(headers.lastHeader("kafka_dlt-exception-class"))
                .map(header -> new String(header.value())).orElse("java.lang.Exception");

        return new RuntimeException("[" + exceptionClass + "] " + exceptionMessage);
    }

    private String extractProfileIdOnly(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(message);

            Long profileId = root.has(PROFILE_ID) && !root.get(PROFILE_ID).isNull()
                ? root.get(PROFILE_ID).asLong()
                : null;

            Long eventId = root.has(PROFILE_ID) && !root.get(PROFILE_ID).isNull()
                ? root.get(PROFILE_ID).asLong()
                : null;

            // JSON 형태로 다시 축소해서 반환
            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put(PROFILE_ID, profileId);
            resultNode.put(EVENT_ID, eventId);

            return objectMapper.writeValueAsString(resultNode);

        } catch (Exception e) {
            // 파싱 실패 시 원본 메시지 일부라도 반환
            return "{\"profileId\":null, \"originalFragment\": \"" + message.substring(0, Math.min(100, message.length())) + "\"}";
        }
    }
}
