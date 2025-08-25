package finpik;

import finpik.entity.enums.CertificateRequirement;
import finpik.entity.enums.Gender;
import finpik.entity.enums.LoanProductBadge;
import finpik.entity.enums.Occupation;
import finpik.dto.LoanProductCreationDto;
import finpik.vo.BankDetails;
import finpik.vo.InterestRate;
import finpik.vo.RepaymentPeriod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<LoanProductBadge> loanProductBadgeList;

    public static LoanProduct of(LoanProductCreationDto dto) {
        return rebuild(dto);
    }

    public static LoanProduct withId(LoanProductCreationDto dto) {
        return rebuild(dto);
    }

    private static LoanProduct rebuild(LoanProductCreationDto dto) {
        BankDetails bankDetails = new BankDetails(
            dto.bankName(),
            dto.bankPhoneNumber(),
            dto.loanAvailableTime()
        );
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
            requirePositive(dto.minCreditScore(), "minCreditScore must not be null."),
            dto.loanProductBadgeList()
        );
    }

    public void changeLoanProductBadgeList(List<LoanProductBadge> loanProductBadgeList) {
        this.loanProductBadgeList = loanProductBadgeList;
    }

    public void changeLoanProductPrerequisite(String loanProductPrerequisite) {
        this.description = new LoanProductDescription(
            loanProductPrerequisite,
            description.getLoanTargetGuide(),
            description.getMaxLoanLimitGuide(),
            description.getRepaymentPeriodGuide(),
            description.getInterestRateGuide(),
            description.getCertificationRequirementGuide(),
            description.getLoanAvailableTimeGuide(),
            description.getRepaymentFeeGuide(),
            description.getDelinquencyInterestRateGuide(),
            description.getNotesOnLoan(),
            description.getPreLoanChecklist()
        );
    }
}
