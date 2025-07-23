package finpik.repository.loanproduct.predicate;

import org.springframework.data.domain.Sort;

public record RecommendedLoanProductPredicate(
    Long profileId,
    Integer page,
    Integer size,
    Sort sort
) {
}
