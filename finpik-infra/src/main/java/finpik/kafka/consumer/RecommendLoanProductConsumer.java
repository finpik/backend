package finpik.kafka.consumer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

import finpik.db.entity.history.dlq.KafkaFailedMessage;
import finpik.db.jpa.repository.history.dlq.KafkaFailedMessageJpaRepository;
import finpik.dto.RecommendedCompleteEvent;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.kafka.dto.RecommendedLoanProductDto;
import finpik.redis.service.loanproduct.LoanProductRedisRepository;
import finpik.redis.service.loanproduct.dto.CachedRecommendedLoanProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendLoanProductConsumer {
    private static final String RECOMMENDATION_TOPIC = "recommendations";
    private static final String LOAN_RECOMMENDER_GROUP_ID = "loan-recommender";
    private final LoanProductRedisRepository loanProductRedisRepository;
    private final KafkaFailedMessageJpaRepository kafkaFailedMessageJpaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    /**
     * @param message
     *            기본 설정 시도 3번, dlq 토픽 서픽스 기본 설정 -dlq 사용
     */
    @RetryableTopic(backoff = @Backoff(delay = 1000, multiplier = 2))
    @KafkaListener(topics = RECOMMENDATION_TOPIC, groupId = LOAN_RECOMMENDER_GROUP_ID)
    public void recommendLoanProduct(String message) {
        RecommendedLoanProductDto loanProductDto = toRecommendedLoanProductDto(message);

        Long profileId = loanProductDto.profileId();

        List<CachedRecommendedLoanProduct> cachedRecommendedLoanProductList = loanProductDto
                .toCachedRecommendedLoanProductList();

        loanProductRedisRepository.cacheRecommendations(profileId, cachedRecommendedLoanProductList);

        RecommendedCompleteEvent event = RecommendedCompleteEvent.of(profileId);
        eventPublisher.publishEvent(event);
    }

    private RecommendedLoanProductDto toRecommendedLoanProductDto(String message) {
        try {
            return objectMapper.readValue(message, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_CONVERT_KAFKA_MESSAGE_LOAN_PRODUCT);
        }
    }

    @DltHandler
    public void handleDlt(String message, ConsumerRecord<?, ?> record) {
        Throwable cause = extractException(record);

        LocalDateTime now = LocalDateTime.now();

        kafkaFailedMessageJpaRepository.save(KafkaFailedMessage.from(record.topic(), message, cause.getMessage(),
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
}
