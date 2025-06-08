package finpik.resolver.loanproduct.resolver.result;

import finpik.entity.enums.LoanPeriodUnit;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import lombok.Getter;

/*
TODO(대출 필수 조건에 대해 어떻게 넣을지 확인 필요)
현재는 해당 부분이 제대로 되어 있지 않음
 */
@Getter
public class LoanProductResult {
    private final String productName;
    private final String bankName;
    private final Integer loanLimitAmount;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Integer loanPeriod;
    private final LoanPeriodUnit loanPeriodUnit;
    private final LoanProductDescriptionResult descriptionResult;

    public LoanProductResult(LoanProductDto dto) {
        productName = dto.getProductName();
        bankName = dto.getBankName();
        loanLimitAmount = dto.getLoanLimitAmount();
        maxInterestRate = dto.getMaxInterestRate();
        minInterestRate = dto.getMinInterestRate();
        loanPeriod = dto.getLoanPeriod();
        loanPeriodUnit = dto.getLoanPeriodUnit();
        descriptionResult = new LoanProductDescriptionResult(dto.getDescription());
    }
}
