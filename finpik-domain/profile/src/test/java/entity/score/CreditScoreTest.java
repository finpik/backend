package entity.score;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.score.CreditScore;
import finpik.error.enums.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreditScoreTest {

    @DisplayName("신용 점수 신용등급 둘다 null일 경우 에러가 발생한다.")
    @Test
    void validateCreditsTest() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new CreditScore(null, null))
            .hasMessage(ErrorCode.CREDITS_CANNOT_BE_NULL.getMessage());
    }

    @DisplayName("신용 점수가 없고 신용 등급이 있다면 신용등급에 맞는 최소 점수로 채워진다.")
    @Test
    void determineCreditScoreByGradeStatusTest() {
        //given
        CreditGradeStatus creditGradeStatus = CreditGradeStatus.EXCELLENT;

        //when
        CreditScore creditScore = new CreditScore(null, creditGradeStatus);

        //then
        assertThat(creditScore.creditScore()).isEqualTo(870);
    }

    @DisplayName("신용 등급이 없고 신용 점수가 있다면 신용 점수에 맞는 신용 등급이 채워진다.")
    @Test
    void determineCreditGradeStatusByScoreTest() {
        //given
        Integer creditScoreInt = 820;

        //when
        CreditScore creditScore = new CreditScore(creditScoreInt, null);

        //then
        assertThat(creditScore.creditGradeStatus()).isEqualTo(CreditGradeStatus.GOOD);
    }
}
