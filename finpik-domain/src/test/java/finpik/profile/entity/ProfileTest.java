package finpik.profile.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.error.enums.ErrorCode;
import finpik.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

class ProfileTest {

    @DisplayName("프로필을 업데이트 할 수 있다.")
    @Test
    void updateProfile() {
        // given
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        Long id = 1L;
        
        Profile profile = getDefaultProfile(user);

        Profile updatingProfile = Profile.withId(
            id,
            10_000_000,
            0,
            0,
            900,
            CreditGradeStatus.EXCELLENT,
            null,
            0,
            null,
            EmploymentForm.CONTRACT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            null,
            "내 전 프로필",
            Occupation.OTHER,
            user,
            ProfileColor.BLUE_TWO
        );

        // when
        profile.updateProfile(updatingProfile);

        // then
        assertAll(() -> assertThat(profile.getOccupation()).isEqualTo(Occupation.OTHER),
                () -> assertThat(profile.getEmploymentForm()).isEqualTo(EmploymentForm.CONTRACT),
                () -> assertThat(profile.getPurposeOfLoan()).isEqualTo(PurposeOfLoan.LOAN_REPAYMENT));
    }

    @DisplayName("순번을 수정할 수 있다.")
    @Test
    void updateSequence() {
        // given
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        Integer seq = 1;
        Profile profile = getDefaultProfile(user);

        // when
        profile.updateSequence(seq);

        // then
        assertThat(profile.getSeq()).isEqualTo(seq);
    }

    @DisplayName("프로필 생성시 EMPLOYEE인데 수입, 직장명, 입사일을 적지 않으면 예외가 발생한다.")
    @Test
    void validateInfoRelatedEmploymentStatus() {
        // given
        // when
        // then
        assertThatThrownBy(
                () -> Profile.withId(
                    1L,
                    10_000_000,
                    0,
                    0,
                    900,
                    CreditGradeStatus.EXCELLENT,
                    null,
                    0,
                    null,
                    EmploymentForm.CONTRACT,
                    LoanProductUsageStatus.NOT_USING,
                    PurposeOfLoan.LOAN_REPAYMENT,
                    null,
                    "내 전 프로필",
                    Occupation.EMPLOYEE,
                    null,
                    ProfileColor.BLUE_TWO
                )).hasMessage(ErrorCode.INVALID_EMPLOYMENT_INFO.getMessage());
    }

    @DisplayName("프로필 생성시 신용 등급와, 신용 점수가 없다면 예외가 발생한다.")
    @Test
    void validateCredits() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Profile.withId(
            1L,
            10_000_000,
            0,
            0,
            null,
            null,
            null,
            0,
            null,
            EmploymentForm.CONTRACT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            null,
            "내 전 프로필",
            Occupation.SELF_EMPLOYED,
            null,
            ProfileColor.BLUE_TWO
        )).hasMessage(ErrorCode.CREDITS_CANNOT_BE_NULL.getMessage());
    }

    @DisplayName("프로필 생성시 신용 점수가 있다면 신용등급이 정해진다.")
    @Test
    void determineCreditGradeStatusByScore() {
        // given
        int score = 900;

        // when
        Profile profile = Profile.withId(
            1L,
            10_000_000,
            0,
            0,
            900,
            null,
            null,
            0,
            null,
            EmploymentForm.CONTRACT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            null,
            "내 전 프로필",
            Occupation.SELF_EMPLOYED,
            null,
            ProfileColor.BLUE_TWO
        );

        // then
        assertThat(profile.getCreditGradeStatus()).isEqualTo(CreditGradeStatus.EXCELLENT);
    }

    @DisplayName("프로필 색상을 변경할 수 있다.")
    @Test
    void changeProfileColor() {
        // given
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        ProfileColor profileColor = ProfileColor.GRAY_TWO;
        Profile profile = getDefaultProfile(user);

        // when
        profile.changeProfileColor(profileColor);

        // then
        assertThat(profile.getProfileColor()).isEqualTo(profileColor);
    }

    private Profile getDefaultProfile(User user) {
        return Profile.withId(
            1L,
            10_000_000,
            0,
            0,
            900,
            CreditGradeStatus.EXCELLENT,
            null,
            0,
            null,
            EmploymentForm.CONTRACT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            null,
            "내 전 프로필",
            Occupation.OTHER,
            user,
            ProfileColor.BLUE_TWO
        );
    }
}
