package finpik.profile.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

class ProfileTest {

    @DisplayName("프로필을 업데이트 할 수 있다.")
    @Test
    void updateProfile() {
        // given
        User user = User.builder().id(1L).build();
        Profile profile = getDefaultProfile(user);
        Profile updatingProfile = Profile.builder().id(1L).occupation(Occupation.SELF_EMPLOYED)
                .employmentForm(EmploymentForm.FULL_TIME).purposeOfLoan(PurposeOfLoan.BUSINESS_FUNDS)
                .desiredLoanAmount(10000000).loanProductUsageStatus(LoanProductUsageStatus.NOT_USING)
                .loanProductUsageCount(0).totalLoanUsageAmount(0).creditScore(900)
                .creditGradeStatus(CreditGradeStatus.EXCELLENT).profileColor(ProfileColor.BLUE_TWO)
                .profileName("내 전 프로필").seq(0).user(user).build();

        // when
        profile.updateProfile(updatingProfile);

        // then
        assertAll(() -> assertThat(profile.getOccupation()).isEqualTo(Occupation.SELF_EMPLOYED),
                () -> assertThat(profile.getEmploymentForm()).isEqualTo(EmploymentForm.FULL_TIME),
                () -> assertThat(profile.getPurposeOfLoan()).isEqualTo(PurposeOfLoan.BUSINESS_FUNDS));
    }

    @DisplayName("순번을 수정할 수 있다.")
    @Test
    void updateSequence() {
        // given
        User user = User.builder().id(1L).build();
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
                () -> Profile.builder().occupation(Occupation.EMPLOYEE).profileColor(ProfileColor.BLUE_TWO).build())
                .hasMessage(ErrorCode.INVALID_EMPLOYMENT_INFO.getMessage());
    }

    @DisplayName("프로필 생성시 신용 등급와, 신용 점수가 없다면 예외가 발생한다.")
    @Test
    void validateCredits() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Profile.builder().occupation(Occupation.SELF_EMPLOYED).build())
                .hasMessage(ErrorCode.CREDITS_CANNOT_BE_NULL.getMessage());
    }

    @DisplayName("프로필 생성시 신용 점수가 있다면 신용등급이 정해진다.")
    @Test
    void determineCreditGradeStatusByScore() {
        // given
        int score = 900;

        // when
        Profile profile = Profile.builder().occupation(Occupation.SELF_EMPLOYED).creditScore(score).build();

        // then
        assertThat(profile.getCreditGradeStatus()).isEqualTo(CreditGradeStatus.EXCELLENT);
    }

    @DisplayName("프로필 색상을 변경할 수 있다.")
    @Test
    void changeProfileColor() {
        // given
        User user = User.builder().id(1L).build();
        ProfileColor profileColor = ProfileColor.GRAY_TWO;
        Profile profile = getDefaultProfile(user);

        // when
        profile.changeProfileColor(profileColor);

        // then
        assertThat(profile.getProfileColor()).isEqualTo(profileColor);
    }

    private Profile getDefaultProfile(User user) {
        return Profile.builder().id(1L).occupation(Occupation.OTHER).employmentForm(EmploymentForm.CONTRACT)
                .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.NOT_USING).loanProductUsageCount(0)
                .totalLoanUsageAmount(0).creditScore(900).creditGradeStatus(CreditGradeStatus.EXCELLENT)
                .profileColor(ProfileColor.BLUE_TWO).profileName("내 전 프로필").seq(0).user(user).build();
    }
}
