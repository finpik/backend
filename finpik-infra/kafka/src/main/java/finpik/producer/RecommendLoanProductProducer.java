package finpik.producer;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import finpik.dto.RecommendLoanProductProfileEvent;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.producer.dlq.KafkaReliableAsyncProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendLoanProductProducer {
    private final KafkaReliableAsyncProducerService kafkaReliableAsyncProducerService;
    private static final String RECOMMENDATION_TOPIC = "recommendations";
    private final ObjectMapper objectMapper;
    private static final int MAX_ATTEMPTS = 3;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageForRecommendLoanProduct(RecommendLoanProductProfileEvent recommendLoanProductProfileEvent) {
        try {
            String message = objectMapper.writeValueAsString(recommendLoanProductProfileEvent);
            kafkaReliableAsyncProducerService.sendAsyncWithRetryAndDlq(RECOMMENDATION_TOPIC, message, MAX_ATTEMPTS);
            log.info("[프로듀서가 보낸]: " + message);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_CONVERT_KAFKA_MESSAGE);
        }
    }
}
