package finpik.db.entity.loanproduct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.enums.LoanPeriodUnit;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.loanproduct.entity.LoanProduct;

class LoanProductEntityTest {

    @DisplayName("LoanProductEntity를 LoanProduct 도메인 객체로 만들 수 있다.")
    @Test
    void toDomain() {
        // given
        LoanProductEntity entity = LoanProductEntity.builder().id(1L).productName("상품 이름").bankName("은행 이름")
                .maxInterestRate(3.5f).minInterestRate(2.0f).maxCreditLine(900).loanPeriod(5).maxCreditGrade(900)
                .minCreditGrade(500).age(20).loanLimitAmount(1000)
                .description(LoanProductDescriptionEntity.builder().build()).loanPeriodUnit(LoanPeriodUnit.YEAR)
                .occupation(Occupation.EMPLOYEE).purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).build();

        // when
        LoanProduct result = entity.toDomain();

        // then
        assertAll(() -> assertThat(result).isInstanceOf(LoanProduct.class),
                () -> assertThat(result.getId()).isEqualTo(entity.getId()),
                () -> assertThat(result.getProductName()).isEqualTo(entity.getProductName()),
                () -> assertThat(result.getBankName()).isEqualTo(entity.getBankName()),
                () -> assertThat(result.getMaxInterestRate()).isEqualTo(entity.getMaxInterestRate()),
                () -> assertThat(result.getMinInterestRate()).isEqualTo(entity.getMinInterestRate()),
                () -> assertThat(result.getMaxCreditLine()).isEqualTo(entity.getMaxCreditLine()),
                () -> assertThat(result.getLoanPeriod()).isEqualTo(entity.getLoanPeriod()),
                () -> assertThat(result.getMaxCreditGrade()).isEqualTo(entity.getMaxCreditGrade()),
                () -> assertThat(result.getMinCreditGrade()).isEqualTo(entity.getMinCreditGrade()),
                () -> assertThat(result.getAge()).isEqualTo(entity.getAge()),
                () -> assertThat(result.getLoanLimitAmount()).isEqualTo(entity.getLoanLimitAmount()),
                () -> assertThat(result.getLoanPeriodUnit()).isEqualTo(entity.getLoanPeriodUnit()),
                () -> assertThat(result.getOccupation()).isEqualTo(entity.getOccupation()),
                () -> assertThat(result.getPurposeOfLoan()).isEqualTo(entity.getPurposeOfLoan()),
                () -> assertThat(result.getDescription()).isNotNull());
    }
}
