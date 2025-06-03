package finpik.kafka.consumer.mapper;

import java.util.List;

import finpik.dto.RecommendedCompleteEvent;
import finpik.redis.service.loanproduct.dto.CachedRecommendedLoanProduct;

public class RecommendedContentMapper {
    public static List<RecommendedCompleteEvent.RecommendedCompleteEventContent> fromCached(
            List<CachedRecommendedLoanProduct> cachedRecommendedLoanProductList) {
        return cachedRecommendedLoanProductList.stream()
                .map(product -> RecommendedCompleteEvent.RecommendedCompleteEventContent.of(product.loanProductId(),
                        product.productName(), product.minInterestRate(), product.maxInterestRate(),
                        product.loanLimitAmount()))
                .toList();
    }
}
