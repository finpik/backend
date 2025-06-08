package entity.loanproduct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.loanproduct.LoanProductDescriptionEntity;
import finpik.loanproduct.LoanProductDescription;

class LoanProductDescriptionEntityTest {

    @DisplayName("LoanProductDescriptionEntity LoanProductDescription 도메인 객체로 만들 수 있다.")
    @Test
    void toDomain() {
        // given
        LoanProductDescriptionEntity loanProductDescriptionEntity = LoanProductDescriptionEntity.builder().id(1L)
                .loanRequirement("테스트 요구사항").interestRateGuide("테스트 금리 가이드").repaymentPeriod("테스트 상환 기간 안내")
                .creditLoanGuide("테스트 신용 대출 가이드").loanFeeGuide("테스트 대출 금액 가이드").notesOnLoan("테스트 대출 유의사항").build();

        // when
        LoanProductDescription result = loanProductDescriptionEntity.toDomain();

        // then
        assertAll(() -> assertThat(result).isInstanceOf(LoanProductDescription.class),
                () -> assertThat(result.getId()).isEqualTo(loanProductDescriptionEntity.getId()),
                () -> assertThat(result.getLoanRequirement())
                        .isEqualTo(loanProductDescriptionEntity.getLoanRequirement()),
                () -> assertThat(result.getInterestRateGuide())
                        .isEqualTo(loanProductDescriptionEntity.getInterestRateGuide()),
                () -> assertThat(result.getRepaymentPeriod())
                        .isEqualTo(loanProductDescriptionEntity.getRepaymentPeriod()),
                () -> assertThat(result.getCreditLoanGuide())
                        .isEqualTo(loanProductDescriptionEntity.getCreditLoanGuide()),
                () -> assertThat(result.getLoanFeeGuide()).isEqualTo(loanProductDescriptionEntity.getLoanFeeGuide()),
                () -> assertThat(result.getNotesOnLoan()).isEqualTo(loanProductDescriptionEntity.getNotesOnLoan()));
    }
}
