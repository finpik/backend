package finpik.entity.loanproduct;

import finpik.entity.enums.LoanPeriodUnit;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.entity.LoanProductDescription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_description_id")
    private LoanProductDescriptionEntity description;

    @Enumerated(EnumType.STRING)
    private LoanPeriodUnit loanPeriodUnit;

    @Enumerated(EnumType.STRING)
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    private PurposeOfLoan purposeOfLoan;

    @Builder
    public LoanProductEntity(Long id, String productName, String bankName, Float maxInterestRate, Float minInterestRate,
            Integer maxCreditLine, Integer loanPeriod, Integer maxCreditGrade, Integer minCreditGrade, Integer age,
            Integer loanLimitAmount, LoanProductDescriptionEntity description, LoanPeriodUnit loanPeriodUnit,
            Occupation occupation, PurposeOfLoan purposeOfLoan) {
        this.id = id;
        this.productName = productName;
        this.bankName = bankName;
        this.maxInterestRate = maxInterestRate;
        this.minInterestRate = minInterestRate;
        this.maxCreditLine = maxCreditLine;
        this.loanPeriod = loanPeriod;
        this.maxCreditGrade = maxCreditGrade;
        this.minCreditGrade = minCreditGrade;
        this.age = age;
        this.loanLimitAmount = loanLimitAmount;
        this.description = description;
        this.loanPeriodUnit = loanPeriodUnit;
        this.occupation = occupation;
        this.purposeOfLoan = purposeOfLoan;
    }

    public LoanProduct toDomain() {
        LoanProductDescription descriptionDomain = description.toDomain();

        return LoanProduct.builder().id(id).productName(productName).bankName(bankName).maxInterestRate(maxInterestRate)
                .minInterestRate(minInterestRate).maxCreditLine(maxCreditLine).loanPeriod(loanPeriod)
                .maxCreditGrade(maxCreditGrade).minCreditGrade(minCreditGrade).age(age).loanLimitAmount(loanLimitAmount)
                .description(descriptionDomain).loanPeriodUnit(loanPeriodUnit).occupation(occupation)
                .purposeOfLoan(purposeOfLoan).build();
    }
}
