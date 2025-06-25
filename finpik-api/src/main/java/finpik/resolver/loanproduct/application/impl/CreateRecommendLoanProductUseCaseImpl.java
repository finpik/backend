package finpik.resolver.loanproduct.application.impl;

import finpik.dto.CreateRecommendedLoanProductEvent;
import finpik.loanproduct.RecommendedLoanProduct;
import finpik.repository.loanproduct.RecommendedLoanProductCacheRepository;
import finpik.repository.loanproduct.RecommendedLoanProductRepository;
import finpik.resolver.loanproduct.application.CreateRecommendLoanProductUseCase;
import finpik.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRecommendLoanProductUseCaseImpl implements
    CreateRecommendLoanProductUseCase
{
    private final RecommendedLoanProductRepository recommendedLoanProductRepository;
    private final SseEmitterService sseEmitterService;
    private final RecommendedLoanProductCacheRepository recommendedLoanProductCacheRepository;

    @Async
    @Override
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(CreateRecommendedLoanProductEvent event) {
        //TODO 혹여 해당 부분이 실패할 경우 어떻게 해야할지 강구

        List<RecommendedLoanProduct> recommendedLoanProductList = event.contentList().stream()
            .map(content ->
            RecommendedLoanProduct.of(
                event.profileId(),
                content.loanProductId(),
                content.productName(),
                content.maxInterestRate(),
                content.minInterestRate(),
                content.loanLimitAmount()
            )
        ).toList();

        List<RecommendedLoanProduct> recommendedLoanProducts =
            recommendedLoanProductRepository.saveAll(event.profileId(), recommendedLoanProductList);

        recommendedLoanProductCacheRepository.cacheAsync(event.profileId(), recommendedLoanProducts);

        sseEmitterService.notifyRecommendationCompleted(
            event.eventId(),
            event.profileId(),
            recommendedLoanProductList
        );
    }
}
