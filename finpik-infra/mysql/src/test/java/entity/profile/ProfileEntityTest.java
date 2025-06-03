package entity.profile;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.profile.ProfileEntity;
import finpik.entity.user.UserEntity;
import finpik.profile.entity.Profile;
import finpik.user.entity.User;

class ProfileEntityTest {

    @DisplayName("Profile과 UserEntity로 ProfileEntity를 만들 수 있다.")
    @Test
    void from() {
        // given
        Profile profile = getDefaultProfile();
        UserEntity userEntity = UserEntity.builder().build();

        // when
        ProfileEntity result = ProfileEntity.from(profile, userEntity);

        // then
        assertThat(result).isInstanceOf(ProfileEntity.class);
    }

    @DisplayName("ProfileEntity로 Profile 도메인 객체를 만들 수 있다.")
    @Test
    void toDomain() {
        // given
        Profile profile = getDefaultProfile();
        UserEntity userEntity = UserEntity.builder().build();

        ProfileEntity entity = ProfileEntity.from(profile, userEntity);
        // when
        Profile result = entity.toDomain();

        // then
    }

    private Profile getDefaultProfile() {
        User user = User.builder().id(1L).build();

        return Profile.builder().id(1L).occupation(Occupation.OTHER).employmentForm(EmploymentForm.CONTRACT)
                .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.NOT_USING).loanProductUsageCount(0)
                .totalLoanUsageAmount(0).creditScore(900).creditGradeStatus(CreditGradeStatus.EXCELLENT)
                .profileColor(ProfileColor.BLUE_TWO).profileName("내 전 프로필").seq(0).user(user).build();
    }
}
