package finpik.loanproduct;

import finpik.entity.enums.CertificateRequirement;
import finpik.entity.enums.Gender;
import finpik.entity.enums.Occupation;
import finpik.loanproduct.dto.LoanProductCreationDto;
import finpik.loanproduct.vo.BankDetails;
import finpik.loanproduct.vo.InterestRate;
import finpik.loanproduct.vo.RepaymentPeriod;
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
    private RepaymentPeriod repaymentPeriod;
    private BankDetails bankDetails;
    private InterestRate interestRate;
    private Integer minAge;
    private Integer maxAge;
    private Gender gender;
    private Long maxLoanLimitAmount;
    private LoanProductDescription description;
    private CertificateRequirement certificateRequirement;
    private Occupation occupation;
    private String url;
    private Integer minCreditScore;

    public static LoanProduct of(LoanProductCreationDto dto) {
        return create(dto);
    }

    public static LoanProduct withId(LoanProductCreationDto dto) {
        return create(dto);
    }

    private static LoanProduct create(LoanProductCreationDto dto) {
        BankDetails bankDetails = new BankDetails(dto.bankName(), dto.bankPhoneNumber(), dto.loanAvailableTime());
        InterestRate interestRate = new InterestRate(dto.maxInterestRate(), dto.minInterestRate());
        RepaymentPeriod repaymentPeriod = new RepaymentPeriod(dto.repaymentPeriod(), dto.repaymentPeriodUnit());
        LoanProductDescription description = new LoanProductDescription(
            dto.loanPrerequisite(), dto.loanTargetGuide(), dto.loanLimitGuide(),
            dto.repaymentPeriodGuide(), dto.interestRateGuide(), dto.certificationRequirementGuide(),
            dto.loanAvailableTimeGuide(), dto.repaymentFeeGuide(), dto.delinquencyInterestRateGuide(),
            dto.notesOnLoan(), dto.preLoanChecklist()
        );


        return new LoanProduct(
            dto.id(),
            require(dto.productName(), "productName must not be null."),
            repaymentPeriod,
            bankDetails,
            interestRate,
            dto.minAge(),
            dto.maxAge(),
            require(dto.genderLimit(), "genderLimit must not be null."),
            dto.maxLoanLimitAmount(),
            description,
            dto.certificateRequirement(),
            require(dto.occupation(), "occupation must not be null."),
            dto.url(),
            requirePositive(dto.minCreditScore(), "minCreditScore must not be null.")
        );
    }
}
