package finpik.loanproduct.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class InterestRateTest {

    @DisplayName("기본 생성 테스트")
    @Test
    void create() {
        //given
        Float maxInterestRate = 10.0f;
        Float minInterestRate = 7.0f;

        //when
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);

        //then
        assertAll(
            () -> assertThat(interestRate.maxInterestRate()).isEqualTo(maxInterestRate),
            () -> assertThat(interestRate.minInterestRate()).isEqualTo(minInterestRate)
        );
    }

    @DisplayName("최소 금리가 음수일 경우 에러가 발생한다.")
    @Test
    void exceptionWhenMinInterestRateIsNegative() {
        //given
        Float negativeMinInterestRate = -7.0f;
        Float maxInterestRate = 10.0f;

        //when
        //then
        assertThatThrownBy(() -> new InterestRate(maxInterestRate, negativeMinInterestRate))
            .hasMessage("minInterestRate must not be negative");
    }

    @DisplayName("최대 금리가 음수일 경우 에러가 발생한다.")
    @Test
    void exceptionWhenMaxInterestRateIsNegative() {
        //given
        Float minInterestRate = 7.0f;
        Float negativeMaxInterestRate = -10.0f;

        //when
        //then
        assertThatThrownBy(() -> new InterestRate(negativeMaxInterestRate, minInterestRate))
            .hasMessage("maxInterestRate must not be negative");
    }
}
