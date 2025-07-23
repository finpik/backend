package finpik.resolver.loanproduct.resolver.result;

import java.util.List;

public record RecommendedLoanProductResultList(
    List<RecommendedLoanProductResult> content,
    Boolean hasNext,
    Integer page,
    Integer size
) {
}
