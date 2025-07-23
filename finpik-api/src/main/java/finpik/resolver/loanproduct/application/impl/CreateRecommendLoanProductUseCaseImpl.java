package finpik.resolver.loanproduct.application.impl;

import finpik.RecommendedLoanProduct;
import finpik.dto.CreateRecommendedLoanProductEvent;
import finpik.repository.loanproduct.LoanProductRepository;
import finpik.resolver.loanproduct.application.CreateRecommendLoanProductUseCase;
import finpik.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRecommendLoanProductUseCaseImpl implements
    CreateRecommendLoanProductUseCase
{
    private final LoanProductRepository loanProductRepository;
    private final SseEmitterService sseEmitterService;

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
                content.bankName(),
                content.productName(),
                content.maxInterestRate(),
                content.minInterestRate(),
                content.loanLimitAmount(),
                content.similarity()
            )
        ).toList();

        loanProductRepository.saveAllRecommendedLoanProduct(event.profileId(), recommendedLoanProductList);

        sseEmitterService.notifyRecommendationCompleted(
            event.eventId(),
            event.profileId(),
            recommendedLoanProductList
        );
    }
}
