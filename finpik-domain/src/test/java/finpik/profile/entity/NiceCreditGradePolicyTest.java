package finpik.profile.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import finpik.entity.enums.CreditGradeStatus;
import finpik.error.enums.ErrorCode;

class NiceCreditGradePolicyTest {

    @DisplayName("점수에 따라 신용 등급이 결정된다. NICE 기준")
    @MethodSource("provideScoresAndStatusExpected")
    @ParameterizedTest(name = "[{index}] test with value: {0}")
    void fromScore(int score, CreditGradeStatus expected) {
        // given
        // when
        CreditGradeStatus creditGradeStatus = NiceCreditGradePolicy.fromScore(score);

        // then
        assertThat(creditGradeStatus).isEqualTo(expected);
    }

    @DisplayName("점수가 점수 레인지에 들어가지 않을 경우 에러가 발생한다. OUT_OF_RANGE_CREDIT_GRADE_STATUS")
    @ValueSource(ints = {-1, 1001})
    @ParameterizedTest
    void fromScoreWithOutOfRange(int score) {
        // given
        // when
        // then
        assertThatThrownBy(() -> NiceCreditGradePolicy.fromScore(score))
                .hasMessage(ErrorCode.OUT_OF_RANGE_CREDIT_GRADE_STATUS.getMessage());
    }

    @DisplayName("등급에따라 설명을 받을 수 있다.")
    @MethodSource("provideStatusAndDescriptionExpected")
    @ParameterizedTest(name = "[{index}] test with value: {0}")
    void getDescription(CreditGradeStatus status, String expected) {
        // given
        // when
        String description = NiceCreditGradePolicy.getDescription(status);

        // then
        assertThat(description).isEqualTo(expected);
    }

    @DisplayName("")
    @MethodSource("provideStatusAndRangeExpected")
    @ParameterizedTest(name = "[{index}] test with value: {0}")
    void getRange(CreditGradeStatus status, NiceCreditGradePolicy.GradeRange expected) {
        // given
        // when
        NiceCreditGradePolicy.GradeRange result = NiceCreditGradePolicy.getRange(status);

        // then
        assertAll(() -> assertThat(result.min()).isEqualTo(expected.min()),
                () -> assertThat(result.max()).isEqualTo(expected.max()),
                () -> assertThat(result.description()).isEqualTo(expected.description()));
    }

    private static Stream<Arguments> provideScoresAndStatusExpected() {
        return Stream.of(Arguments.of(870, CreditGradeStatus.EXCELLENT), Arguments.of(805, CreditGradeStatus.GOOD),
                Arguments.of(665, CreditGradeStatus.FAIR), Arguments.of(515, CreditGradeStatus.POOR),
                Arguments.of(0, CreditGradeStatus.VERY_POOR));
    }

    private static Stream<Arguments> provideStatusAndDescriptionExpected() {
        return Stream.of(Arguments.of(CreditGradeStatus.EXCELLENT, "아주 안정적이에요"),
                Arguments.of(CreditGradeStatus.GOOD, "꽤 괜찮아요"), Arguments.of(CreditGradeStatus.FAIR, "조금 불안정해요"),
                Arguments.of(CreditGradeStatus.POOR, "가끔 어려워요"), Arguments.of(CreditGradeStatus.VERY_POOR, "관리가 필요해요"));
    }

    private static Stream<Arguments> provideStatusAndRangeExpected() {
        NiceCreditGradePolicy.GradeRange excellentGradeRange = new NiceCreditGradePolicy.GradeRange(870, 1000,
                "아주 안정적이에요");
        NiceCreditGradePolicy.GradeRange goodGradeRange = new NiceCreditGradePolicy.GradeRange(805, 869, "꽤 괜찮아요");
        NiceCreditGradePolicy.GradeRange fairGradeRange = new NiceCreditGradePolicy.GradeRange(665, 804, "조금 불안정해요");
        NiceCreditGradePolicy.GradeRange poorGradeRange = new NiceCreditGradePolicy.GradeRange(515, 664, "가끔 어려워요");
        NiceCreditGradePolicy.GradeRange veryPoorGradeRange = new NiceCreditGradePolicy.GradeRange(0, 514, "관리가 필요해요");

        return Stream.of(Arguments.of(CreditGradeStatus.EXCELLENT, excellentGradeRange),
                Arguments.of(CreditGradeStatus.GOOD, goodGradeRange),
                Arguments.of(CreditGradeStatus.FAIR, fairGradeRange),
                Arguments.of(CreditGradeStatus.POOR, poorGradeRange),
                Arguments.of(CreditGradeStatus.VERY_POOR, veryPoorGradeRange));
    }
}
