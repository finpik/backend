package entity.loanproduct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import finpik.entity.enums.CertificateRequirement;
import finpik.entity.enums.Gender;
import finpik.loanproduct.vo.InterestRate;
import finpik.loanproduct.vo.RepaymentPeriod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.enums.RepaymentPeriodUnit;
import finpik.entity.enums.Occupation;
import finpik.entity.loanproduct.LoanProductDescriptionEntity;
import finpik.entity.loanproduct.LoanProductEntity;
import finpik.loanproduct.LoanProduct;

class LoanProductEntityTest {

    @DisplayName("LoanProductEntity를 LoanProduct 도메인 객체로 만들 수 있다.")
    @Test
    void toDomain() {
        // given
        LoanProductDescriptionEntity description = new LoanProductDescriptionEntity(
            "필수조건", "대상 가이드", "한도 가이드", "상환기간 가이드", "금리 가이드",
            "이용시간 가이드", "상환수수료 가이드", "연체이자 가이드",
            "주의사항", "대출 신청전 확인", "인증서 가이드"
        );

        LoanProductEntity entity = LoanProductEntity.builder()
            .productName("상품 이름")
            .bankName("은행 이름")
            .maxInterestRate(3.5f)
            .minInterestRate(2.0f)
            .repaymentPeriod(5)
            .minAge(20)
            .maxAge(35)
            .genderLimit(Gender.MALE)
            .maxLoanLimitAmount(1000L)
            .description(description)
            .minCreditScore(0)
            .repaymentPeriodUnit(RepaymentPeriodUnit.YEAR)
            .certificateRequirement(CertificateRequirement.YES)
            .occupation(Occupation.EMPLOYEE)
            .url("www.test.url")
            .build();

        // when
        LoanProduct result = entity.toDomain();

        // then
        InterestRate interestRate = result.getInterestRate();
        RepaymentPeriod repaymentPeriod = result.getRepaymentPeriod();

        assertAll(
            () -> assertThat(result).isInstanceOf(LoanProduct.class),
            () -> assertThat(result.getId()).isEqualTo(entity.getId()),
            () -> assertThat(result.getProductName()).isEqualTo(entity.getProductName()),
            () -> assertThat(interestRate.maxInterestRate()).isEqualTo(entity.getMaxInterestRate()),
            () -> assertThat(interestRate.minInterestRate()).isEqualTo(entity.getMinInterestRate()),
            () -> assertThat(repaymentPeriod.repaymentPeriod()).isEqualTo(entity.getRepaymentPeriod()),
            () -> assertThat(result.getOccupation()).isEqualTo(entity.getOccupation()),
            () -> assertThat(result.getDescription()).isNotNull()
        );
    }
}
