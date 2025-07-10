package entity.policy;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.policy.NiceCreditGradePolicy;
import finpik.error.enums.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class NiceCreditGradePolicyTest {
    @DisplayName("신용 점수에 따라 싱용등급이 결정되고 그 이외의 값은 에러가 발생한다 0~1000.")
    @Test
    void fromScoreTest() {
        //given
        Integer excellentScore = 1000;
        Integer goodScore = 869;
        Integer fairScore = 804;
        Integer poorScore = 664;
        Integer veryPoorScore = 514;
        Integer overTheMaxScore = 1001;
        Integer underTheMinScore = -1;

        //when
        CreditGradeStatus excellentCreditGradeStatus = NiceCreditGradePolicy.fromScore(excellentScore);
        CreditGradeStatus goodCreditGradeStatus = NiceCreditGradePolicy.fromScore(goodScore);
        CreditGradeStatus fairCreditGradeStatus = NiceCreditGradePolicy.fromScore(fairScore);
        CreditGradeStatus poorCreditGradeStatus = NiceCreditGradePolicy.fromScore(poorScore);
        CreditGradeStatus veryPoorCreditGradeStatus = NiceCreditGradePolicy.fromScore(veryPoorScore);

        //then
        assertAll(
            () -> assertThat(excellentCreditGradeStatus).isEqualTo(CreditGradeStatus.EXCELLENT),
            () -> assertThat(goodCreditGradeStatus).isEqualTo(CreditGradeStatus.GOOD),
            () -> assertThat(fairCreditGradeStatus).isEqualTo(CreditGradeStatus.FAIR),
            () -> assertThat(poorCreditGradeStatus).isEqualTo(CreditGradeStatus.POOR),
            () -> assertThat(veryPoorCreditGradeStatus).isEqualTo(CreditGradeStatus.VERY_POOR),
            () -> assertThatThrownBy(() -> NiceCreditGradePolicy.fromScore(overTheMaxScore))
                .hasMessage(ErrorCode.OUT_OF_RANGE_CREDIT_GRADE_STATUS.getMessage()),
            () -> assertThatThrownBy(() -> NiceCreditGradePolicy.fromScore(underTheMinScore))
                .hasMessage(ErrorCode.OUT_OF_RANGE_CREDIT_GRADE_STATUS.getMessage())
        );
    }

    @DisplayName("등급이 있다면 해당 등급의 최소 최대값을 알 수 있다.")
    @Test
    void getRangeTest() {
        //given
        CreditGradeStatus excellentCreditGradeStatus = CreditGradeStatus.EXCELLENT;
        CreditGradeStatus goodCreditGradeStatus = CreditGradeStatus.GOOD;
        CreditGradeStatus fairCreditGradeStatus = CreditGradeStatus.FAIR;
        CreditGradeStatus poorCreditGradeStatus = CreditGradeStatus.POOR;
        CreditGradeStatus veryPoorCreditGradeStatus = CreditGradeStatus.VERY_POOR;

        //when
        NiceCreditGradePolicy.GradeRange excellentRange = NiceCreditGradePolicy.getRange(excellentCreditGradeStatus);
        NiceCreditGradePolicy.GradeRange goodRange = NiceCreditGradePolicy.getRange(goodCreditGradeStatus);
        NiceCreditGradePolicy.GradeRange fairRange = NiceCreditGradePolicy.getRange(fairCreditGradeStatus);
        NiceCreditGradePolicy.GradeRange poorRange = NiceCreditGradePolicy.getRange(poorCreditGradeStatus);
        NiceCreditGradePolicy.GradeRange veryPoorRange = NiceCreditGradePolicy.getRange(veryPoorCreditGradeStatus);

        //then
        assertAll(
            () -> assertThat(excellentRange.max()).isEqualTo(1000),
            () -> assertThat(excellentRange.min()).isEqualTo(870),
            () -> assertThat(goodRange.max()).isEqualTo(869),
            () -> assertThat(goodRange.min()).isEqualTo(805),
            () -> assertThat(fairRange.max()).isEqualTo(804),
            () -> assertThat(fairRange.min()).isEqualTo(665),
            () -> assertThat(poorRange.max()).isEqualTo(664),
            () -> assertThat(poorRange.min()).isEqualTo(515),
            () -> assertThat(veryPoorRange.max()).isEqualTo(514),
            () -> assertThat(veryPoorRange.min()).isEqualTo(0)
        );
    }
}
