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
        assertThat(result).isInstanceOf(Profile.class);
    }

    private Profile getDefaultProfile() {
        User user = User.builder().id(1L).build();

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
