package finpik.resolver.loanproduct.application.impl;

import finpik.RecommendedLoanProduct;
import finpik.dto.CreateRecommendedLoanProductEvent;
import finpik.entity.enums.LoanProductBadge;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.loanproduct.LoanProductRepository;
import finpik.repository.profile.ProfileRepository;
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
    private final ProfileRepository profileRepository;
    private final SseEmitterService sseEmitterService;

    @Async
    @Override
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(CreateRecommendedLoanProductEvent event) {
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

        List<RecommendedLoanProduct> recommendedLoanProducts =
            loanProductRepository.saveAllRecommendedLoanProduct(event.profileId(), recommendedLoanProductList);

        log.info("Created {} recommended loan product events", recommendedLoanProducts.stream().filter(it ->
            it.getLoanProductBadges().contains(LoanProductBadge.LOWEST_MIN_INTEREST_RATE)
        ).toList().size());

        RecommendedLoanProduct first = recommendedLoanProducts.stream()
            .filter(it ->
                it.getLoanProductBadges().stream()
                    .anyMatch(badge -> badge == LoanProductBadge.LOWEST_MIN_INTEREST_RATE)
            ).findFirst().orElseThrow(() -> new BusinessException(ErrorCode.EMPTY_BADGES));

        profileRepository.updateProfileAfterRecommend(
            event.profileId(), recommendedLoanProductList.size(), first.getMinInterestRate()
        );

        sseEmitterService.notifyRecommendationCompleted(
            event.eventId(),
            event.profileId(),
            recommendedLoanProductList
        );
    }
}
