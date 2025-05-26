package finpik.loanproduct.entity;

import finpik.entity.enums.LoanPeriodUnit;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import lombok.Builder;
import lombok.Getter;

/**
 * 저장은 대부분의 경우 Excel 저장으로
 */
@Getter
@Builder
public class LoanProduct {
    private Long id;
    private String productName;
    private String bankName;
    private Float maxInterestRate;
    private Float minInterestRate;
    private Integer maxCreditLine;
    private Integer loanPeriod;
    private Integer maxCreditGrade;
    private Integer minCreditGrade;
    private Integer age;
    private Integer loanLimitAmount;
    private LoanProductDescription description;
    private LoanPeriodUnit loanPeriodUnit;
    private Occupation occupation;
    private PurposeOfLoan purposeOfLoan;
}
