package finpik.kafka.producer;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.kafka.dto.RecommendLoanProductProfileDto;
import finpik.kafka.producer.dlq.KafkaReliableAsyncProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendLoanProductProducer {
    private final KafkaReliableAsyncProducerService kafkaReliableAsyncProducerService;
    private static final String RECOMMENDATION_TOPIC = "recommendations";
    private final ObjectMapper objectMapper;
    private static final int MAX_ATTEMPTS = 3;

    public void sendMessageForRecommendLoanProduct(RecommendLoanProductProfileDto recommendLoanProductProfileDto) {
        try {
            String message = objectMapper.writeValueAsString(recommendLoanProductProfileDto);
            kafkaReliableAsyncProducerService.sendAsyncWithRetryAndDlq(RECOMMENDATION_TOPIC, message, MAX_ATTEMPTS);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_CONVERT_KAFKA_MESSAGE);
        }
    }
}
