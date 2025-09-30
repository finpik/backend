package entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import finpik.entity.Profile;
import finpik.entity.User;
import finpik.entity.enums.Gender;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.RegistrationType;
import finpik.entity.policy.ProfileCreationSpec;
import finpik.entity.policy.ProfileUpdateSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.error.enums.ErrorCode;

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

        Profile profile = getDefaultProfile(user);
        ProfileUpdateSpec spec = new ProfileUpdateSpec(
            1L,
            Occupation.EMPLOYEE,
            EmploymentForm.FULL_TIME,
            1000000000,
            LocalDate.now(),
            PurposeOfLoan.LOAN_REPAYMENT,
            1000000000,
            LoanProductUsageStatus.NOT_USING,
            0,
            0,
            900,
            CreditGradeStatus.EXCELLENT,
            "test profile",
            null,
            null
        );

        // when
        profile.updateProfile(spec);

        // then
        assertAll(() -> assertThat(profile.getOccupationDetail().getOccupation()).isEqualTo(Occupation.EMPLOYEE),
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

    @DisplayName("프로필 생성시 신용 등급와, 신용 점수가 없다면 예외가 발생한다.")
    @Test
    void validateCredits() {
        // given
        ProfileCreationSpec spec = ProfileCreationSpec.rebuild(
            1L,
            100,
            100,
            100,
            null,
            "profile name",
            0,
            null,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            ProfileColor.GRAY,
            1000,
            LocalDate.now(),
            LocalDate.now(),
            Occupation.EMPLOYEE,
            null,
            EmploymentForm.CONTRACT,
            null,
            0,
            0.0f,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // when
        // then
        assertThatThrownBy(() -> Profile.withId(spec))
            .hasMessage(ErrorCode.CREDITS_CANNOT_BE_NULL.getMessage());
    }

    @DisplayName("프로필 생성시 신용 점수가 있다면 신용등급이 정해진다.")
    @Test
    void determineCreditGradeStatusByScore() {
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        // given
        ProfileCreationSpec spec = ProfileCreationSpec.rebuild(
            1L,
            100,
            100,
            100,
            1000,
            "profile name",
            0,
            CreditGradeStatus.EXCELLENT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            ProfileColor.GRAY,
            1000,
            LocalDate.now(),
            LocalDate.now(),
            Occupation.EMPLOYEE,
            null,
            EmploymentForm.CONTRACT,
            user,
            0,
            0.0f,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // when
        Profile profile = Profile.withId(spec);

        // then
        assertThat(profile.getCreditScore().creditGradeStatus()).isEqualTo(CreditGradeStatus.EXCELLENT);
    }

    @DisplayName("프로필 색상을 변경할 수 있다.")
    @Test
    void changeProfileColor() {
        // given
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        ProfileColor profileColor = ProfileColor.GRAY;
        Profile profile = getDefaultProfile(user);

        // when
        profile.changeProfileColor(profileColor);

        // then
        assertThat(profile.getProfileColor()).isEqualTo(profileColor);
    }

    private Profile getDefaultProfile(User user) {
        ProfileCreationSpec spec = ProfileCreationSpec.rebuild(
            1L,
            100,
            100,
            100,
            1000,
            "profile name",
            0,
            CreditGradeStatus.EXCELLENT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            ProfileColor.GRAY,
            1000,
            LocalDate.now(),
            LocalDate.now(),
            Occupation.OTHER,
            null,
            null,
            user,
            0,
            0.0f,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        return Profile.withId(spec);
    }
}
