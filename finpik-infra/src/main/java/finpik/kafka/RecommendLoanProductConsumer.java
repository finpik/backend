package finpik.kafka;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import finpik.dto.RecommendedCompleteEvent;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.kafka.dto.RecommendedLoanProductDto;
import finpik.redis.service.loanproduct.LoanProductRedisRepository;
import finpik.redis.service.loanproduct.dto.CachedRecommendedLoanProduct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecommendLoanProductConsumer {
    private static final String RECOMMENDATION_TOPIC = "recommendations";
    private static final String LOAN_RECOMMENDER_GROUP_ID = "loan-recommender";
    private final LoanProductRedisRepository loanProductRedisRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

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
}
