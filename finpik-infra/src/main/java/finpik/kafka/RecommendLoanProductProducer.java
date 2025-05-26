package finpik.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.kafka.dto.RecommendLoanProductProfileDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendLoanProductProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String RECOMMENDATION_TOPIC = "recommendations";
    private final ObjectMapper objectMapper;

    public void sendMessageForRecommendLoanProduct(RecommendLoanProductProfileDto recommendLoanProductProfileDto) {
        String message;

        try {
            message = objectMapper.writeValueAsString(recommendLoanProductProfileDto);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_CONVERT_KAFKA_MESSAGE);
        }

        kafkaTemplate.send(RECOMMENDATION_TOPIC, message);
    }
}
