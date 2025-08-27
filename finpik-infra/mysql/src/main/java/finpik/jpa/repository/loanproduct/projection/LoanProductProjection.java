package finpik.jpa.repository.loanproduct.projection;

import finpik.entity.enums.LoanProductBadge;
import finpik.entity.loanproduct.LoanProductEntity;

import java.util.List;

public record LoanProductProjection(
    LoanProductEntity loanProductEntity,
    List<LoanProductBadge> loanProductBadgeList
) {
}
