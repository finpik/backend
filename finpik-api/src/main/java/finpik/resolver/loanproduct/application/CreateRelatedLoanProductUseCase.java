package finpik.resolver.loanproduct.application;

public interface CreateRelatedLoanProductUseCase {
    void createUserProductViewAsync(Long userId, Long productId);
}
