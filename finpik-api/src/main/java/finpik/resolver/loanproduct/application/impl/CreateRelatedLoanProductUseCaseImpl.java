package finpik.resolver.loanproduct.application.impl;

import finpik.history.entity.UserProductViewHistory;
import finpik.history.repository.UserProductViewHistoryRepository;
import finpik.resolver.loanproduct.application.CreateRelatedLoanProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRelatedLoanProductUseCaseImpl implements CreateRelatedLoanProductUseCase {
    private final UserProductViewHistoryRepository userProductViewHistoryRepository;

    @Async
    @Override
    public void createUserProductViewAsync(Long userId, Long productId) {
        UserProductViewHistory userProductViewHistory = new UserProductViewHistory(userId, productId);

        userProductViewHistoryRepository.save(userProductViewHistory);
    }
}
