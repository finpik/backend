package finpik.resolver.loanproduct.resolver.input;

import finpik.entity.enums.LoanProductBadge;

import java.util.List;

public record UpdateLoanProductAndPrerequisiteInput(
    Long loanProductId,
    List<LoanProductBadge> loanProductBadgeList,
    String loanPrerequisite
) {
}
