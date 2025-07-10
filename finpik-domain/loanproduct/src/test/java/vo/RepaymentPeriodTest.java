package vo;

import finpik.entity.enums.RepaymentPeriodUnit;
import finpik.vo.RepaymentPeriod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class RepaymentPeriodTest {

    @DisplayName("기본 생성 테스트")
    @Test
    void createTest() {
        //given
        Integer repaymentPeriod = 12;
        RepaymentPeriodUnit repaymentPeriodUnit = RepaymentPeriodUnit.MONTH;

        //when
        RepaymentPeriod target = new RepaymentPeriod(repaymentPeriod, repaymentPeriodUnit);

        //then
        assertAll(
            () -> assertThat(target.repaymentPeriod()).isEqualTo(repaymentPeriod),
            () -> assertThat(target.repaymentPeriodUnit()).isEqualTo(repaymentPeriodUnit)
        );
    }

    @DisplayName("상환 기간이 음수거나 Null이라면 에러가 발생한다.")
    @Test
    void exceptionWhenRepaymentPeriodIsNegativeOrNull() {
        //given
        Integer negativeRepaymentPeriod = -12;
        Integer nullRepaymentPeriod = null;

        //when
        //then
        assertAll(
            () -> assertThatThrownBy(() -> new RepaymentPeriod(negativeRepaymentPeriod, RepaymentPeriodUnit.MONTH))
                .hasMessage("LoanPeriod must not be negative"),
            () -> assertThatThrownBy(() -> new RepaymentPeriod(nullRepaymentPeriod, RepaymentPeriodUnit.MONTH))
                .hasMessage("LoanPeriod must not be null")
        );
    }

    @DisplayName("상환 기간 단위가 null이라면 에러가 발생한다.")
    @Test
    void exceptionWhenRepaymentPeriodUnitIsNull() {
        //given
        Integer repaymentPeriod = 12;
        RepaymentPeriodUnit repaymentPeriodUnit = null;

        //when
        //then
        assertThatThrownBy(() -> new RepaymentPeriod(repaymentPeriod, repaymentPeriodUnit))
            .hasMessage("RepaymentPeriodUnit must not be null");
    }
}
