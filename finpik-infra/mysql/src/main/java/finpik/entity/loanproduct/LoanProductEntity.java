package finpik.entity.loanproduct;

import finpik.LoanProduct;
import finpik.entity.enums.CertificateRequirement;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Gender;
import finpik.entity.enums.LoanProductBadge;
import finpik.entity.enums.RepaymentPeriodUnit;
import finpik.entity.enums.Occupation;
import finpik.dto.LoanProductCreationDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 저장은 대부분의 경우 Excel 저장으로
 */
@Entity
@Getter
@Table(name = "loan_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName; //상품명
    private Integer repaymentPeriod; //상환 기간(숫자)
    private String bankName; //은행
    private String bankPhoneNumber; //전화
    private String loanAvailableTime; //이용 시간
    private Float maxInterestRate; //최대 금리
    private Float minInterestRate; //최소 금리
    private Integer minAge;//minAge
    private Integer maxAge;//maxAge
    private Long maxLoanLimitAmount; //대출 한도(숫자)
    @Lob
    private String url; //url
    private Integer minCreditScore;
    private String bankImgUrl;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<LoanProductBadge> loanProductBadgeList;


    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_description_id")
    private LoanProductDescriptionEntity description;

    @Enumerated(EnumType.STRING)
    private RepaymentPeriodUnit repaymentPeriodUnit; //상환 기간 단위

    @Enumerated(EnumType.STRING)
    private Occupation occupation; //대상(필터용)

    @Enumerated(EnumType.STRING)
    private CertificateRequirement certificateRequirement; //인증서 준비 (있을 경우, 없을 경우)

    @Enumerated(EnumType.STRING)
    private Gender genderLimit; //genderLimit

    @Enumerated(EnumType.STRING)
    private EmploymentForm employmentForm;

    @Builder
    public LoanProductEntity(
        String productName, Integer repaymentPeriod, String bankName,
        String bankPhoneNumber, String loanAvailableTime, Float maxInterestRate,
        Float minInterestRate, RepaymentPeriodUnit repaymentPeriodUnit,
        Integer minAge, Integer maxAge, Long maxLoanLimitAmount,
        LoanProductDescriptionEntity description,
        CertificateRequirement certificateRequirement, Gender genderLimit,
        Occupation occupation, String url, Integer minCreditScore, EmploymentForm employmentForm
    ) {
        this.productName = productName;
        this.repaymentPeriod = repaymentPeriod;
        this.bankName = bankName;
        this.bankPhoneNumber = bankPhoneNumber;
        this.loanAvailableTime = loanAvailableTime;
        this.maxInterestRate = maxInterestRate;
        this.minInterestRate = minInterestRate;
        this.repaymentPeriodUnit = repaymentPeriodUnit;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxLoanLimitAmount = maxLoanLimitAmount;
        this.description = description;
        this.certificateRequirement = certificateRequirement;
        this.occupation = occupation;
        this.genderLimit = genderLimit;
        this.url = url;
        this.minCreditScore = minCreditScore;
        this.employmentForm = employmentForm;
    }

    public LoanProduct toDomain() {
        LoanProductCreationDto dto = new LoanProductCreationDto(
            id, productName, repaymentPeriod, bankName, bankPhoneNumber, loanAvailableTime,
            maxInterestRate, minInterestRate, repaymentPeriodUnit,
            minAge, maxAge, genderLimit, maxLoanLimitAmount,
            description.getLoanPrerequisite(), description.getLoanTargetGuide(),
            description.getCertificationRequirementGuide(),
            description.getMaxLoanLimitAmountGuide(), description.getRepaymentPeriodGuide(),
            description.getInterestRateGuide(), description.getLoanAvailableTimeGuide(),
            description.getRepaymentFeeGuide(), description.getDelinquencyInterestRateGuide(),
            description.getNotesOnLoan(), description.getPreLoanChecklist(),
            certificateRequirement, occupation, url, minCreditScore, employmentForm, bankImgUrl,
            loanProductBadgeList
        );

        return LoanProduct.withId(dto);
    }

    public void changeLoanProductBadgeListAndPrerequisite(List<LoanProductBadge> loanProductBadgeList, String loanPrerequisite) {
        this.loanProductBadgeList = loanProductBadgeList;
        this.getDescription().changeLoanPrerequisite(loanPrerequisite);
    }
}
