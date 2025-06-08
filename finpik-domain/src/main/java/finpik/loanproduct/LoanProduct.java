package finpik.loanproduct;

import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.loanproduct.vo.CreditGrade;
import finpik.loanproduct.vo.InterestRate;
import finpik.loanproduct.vo.LoanPeriod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static finpik.util.Preconditions.require;
import static finpik.util.Preconditions.requirePositive;

/**
 * 저장은 대부분의 경우 Excel 저장으로
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanProduct {
    private Long id;
    private String productName;
    private String bankName;
    private InterestRate interestRate;
    private Integer maxCreditLine;
    private LoanPeriod loanPeriod;
    private CreditGrade creditGrade;
    private Integer age;
    private Integer loanLimitAmount;
    private LoanProductDescription description;
    private Occupation occupation;
    private PurposeOfLoan purposeOfLoan;

    public static LoanProduct of(
        String productName, String bankName, InterestRate interestRate,
        Integer maxCreditLine, LoanPeriod loanPeriod,
        CreditGrade creditGrade, Integer age,
        Integer loanLimitAmount, LoanProductDescription description,
        Occupation occupation, PurposeOfLoan purposeOfLoan
    ) {
        return new LoanProduct(
            null,
            require(productName, "대출 상품의 상품명이 비었습니다."),
            require(bankName, "은행명은 필수입니다"),
            interestRate,
            requirePositive(maxCreditLine, "최대 한도는 양수여야 합니다"),
            loanPeriod,
            creditGrade,
            requirePositive(age, "나이는 양수여야 합니다"),
            requirePositive(loanLimitAmount, "대출 한도는 양수여야 합니다"),
            require(description, "설명은 필수입니다"),
            require(occupation, "직업은 필수입니다"),
            require(purposeOfLoan, "대출 목적은 필수입니다")
        );
    }

    public static LoanProduct withId(
        Long id,
        String productName, String bankName, InterestRate interestRate,
        Integer maxCreditLine, LoanPeriod loanPeriod,
        CreditGrade creditGrade, Integer age,
        Integer loanLimitAmount, LoanProductDescription description,
        Occupation occupation, PurposeOfLoan purposeOfLoan
    ) {
        return new LoanProduct(
            require(id, "ID는 null일 수 없습니다."),
            require(productName, "상품명은 필수입니다."),
            require(bankName, "은행명은 필수입니다."),
            interestRate,
            requirePositive(maxCreditLine, "최대 한도는 양수여야 합니다"),
            loanPeriod,
            creditGrade,
            requirePositive(age, "나이는 양수여야 합니다"),
            requirePositive(loanLimitAmount, "대출 한도는 양수여야 합니다"),
            require(description, "설명은 필수입니다"),
            require(occupation, "직업은 필수입니다"),
            require(purposeOfLoan, "대출 목적은 필수입니다")
        );
    }
}
