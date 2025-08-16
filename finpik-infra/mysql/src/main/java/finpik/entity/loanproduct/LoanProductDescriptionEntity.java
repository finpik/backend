package finpik.entity.loanproduct;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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
    private String loanPrerequisite; //필수 조건
    private String loanTargetGuide; //대상 자세히
    private String maxLoanLimitAmountGuide; //대출 한도 자세히
    private String repaymentPeriodGuide; // 상환 기간 자세히
    private String interestRateGuide; //금리 자세히
    private String loanAvailableTimeGuide; //이용시간 자세히
    private String repaymentFeeGuide; //상환 수수료 추가 정보
    private String delinquencyInterestRateGuide; //연체이자 관한 사항
    private String notesOnLoan; //유의 사항
    @Lob
    private String preLoanChecklist; //대출 신청전 확인
    private String certificationRequirementGuide; //인증서 준비

    @Builder
    public LoanProductDescriptionEntity(
        String loanPrerequisite,
        String loanTargetGuide,
        String maxLoanLimitAmountGuide,
        String repaymentPeriodGuide,
        String interestRateGuide,
        String loanAvailableTimeGuide,
        String repaymentFeeGuide,
        String delinquencyInterestRateGuide,
        String notesOnLoan,
        String preLoanChecklist,
        String certificationRequirementGuide
    ) {
        this.loanPrerequisite = loanPrerequisite;
        this.loanTargetGuide = loanTargetGuide;
        this.maxLoanLimitAmountGuide = maxLoanLimitAmountGuide;
        this.repaymentPeriodGuide = repaymentPeriodGuide;
        this.interestRateGuide = interestRateGuide;
        this.loanAvailableTimeGuide = loanAvailableTimeGuide;
        this.repaymentFeeGuide = repaymentFeeGuide;
        this.delinquencyInterestRateGuide = delinquencyInterestRateGuide;
        this.notesOnLoan = notesOnLoan;
        this.preLoanChecklist = preLoanChecklist;
        this.certificationRequirementGuide = certificationRequirementGuide;
    }

    public void changeLoanPrerequisite(String loanPrerequisite) {
        this.loanPrerequisite = loanPrerequisite;
    }
}
