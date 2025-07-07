package finpik.resolver.loanproduct.application;

import finpik.dto.CreateRecommendedLoanProductEvent;

public interface CreateRecommendLoanProductUseCase {
    void execute(CreateRecommendedLoanProductEvent event);
}
