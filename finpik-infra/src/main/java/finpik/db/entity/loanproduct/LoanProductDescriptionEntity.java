package finpik.db.entity.loanproduct;

import finpik.loanproduct.entity.LoanProductDescription;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "loan_product_description")
public class LoanProductDescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanRequirement;

    private String interestRateGuide;

    private String repaymentPeriod;

    private String creditLoanGuide;

    private String loanFeeGuide;

    private String notesOnLoan;

    public LoanProductDescription toDomain() {
        return LoanProductDescription.builder().id(id).loanRequirement(loanRequirement)
                .interestRateGuide(interestRateGuide).repaymentPeriod(repaymentPeriod).creditLoanGuide(creditLoanGuide)
                .loanFeeGuide(loanFeeGuide).notesOnLoan(notesOnLoan).build();
    }
}
