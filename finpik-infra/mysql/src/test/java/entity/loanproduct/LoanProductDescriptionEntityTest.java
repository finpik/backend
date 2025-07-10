package entity.loanproduct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.loanproduct.LoanProductDescriptionEntity;

class LoanProductDescriptionEntityTest {

    @DisplayName("객체 생성 테스트")
    @Test
    void createTest() {
        // given
        LoanProductDescriptionEntity loanProductDescriptionEntity = LoanProductDescriptionEntity.builder()
            .loanPrerequisite("테스트 요구사항").interestRateGuide("테스트 금리 가이드").repaymentPeriodGuide("테스트 상환 기간 안내")
            .maxLoanLimitAmountGuide("테스트 대출 금액 가이드").notesOnLoan("테스트 대출 유의사항").build();

        // when
        // then
        assertAll(
            () -> assertThat(loanProductDescriptionEntity.getLoanPrerequisite()).isEqualTo("테스트 요구사항"),
            () -> assertThat(loanProductDescriptionEntity.getInterestRateGuide()).isEqualTo("테스트 금리 가이드"),
            () -> assertThat(loanProductDescriptionEntity.getRepaymentPeriodGuide()).isEqualTo("테스트 상환 기간 안내"),
            () -> assertThat(loanProductDescriptionEntity.getMaxLoanLimitAmountGuide()).isEqualTo("테스트 대출 금액 가이드"),
            () -> assertThat(loanProductDescriptionEntity.getNotesOnLoan()).isEqualTo("테스트 대출 유의사항")
        );
    }
}
